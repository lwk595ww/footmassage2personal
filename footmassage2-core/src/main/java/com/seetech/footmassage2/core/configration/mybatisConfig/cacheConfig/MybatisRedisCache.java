package com.seetech.footmassage2.core.configration.mybatisConfig.cacheConfig;



import com.seetech.footmassage2.core.util.RedisUtil;
import com.seetech.footmassage2.core.util.SpringUtils;
import com.seetech.util.GlobalThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  重写 二级缓存逻辑 mybaits 基于redis做二级缓存
 */
@Slf4j
public class MybatisRedisCache implements Cache {

    /**
     * 注意，这里无法通过@Autowired等注解的方式注入bean,只能手动获取
     */
    private RedisUtil redisUtil;

    //private HttpServletRequest request;

    private void getRedisUtil() {
        redisUtil = (RedisUtil) SpringUtils.getBean("redisUtil");
        //通过请求上下文来获取当前HttpServletRequest对象
        //request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

    }

    //全局基础key (多租户需在基础上拼接)
    String id;

    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        log.info("存入缓存");

        //查询出当前该租户（通过调用的接口来获取租户）
        //String tenantId = (String) request.getSession().getAttribute("tenantId");
        //从 ThreadLocal 中获取到 tenantId
        String tenantId = (String) GlobalThreadLocalUtil.get("tenantId");

        if (!StringUtils.isEmpty(tenantId)) {
            //写操作加锁
            readWriteLock.writeLock().lock();
            if (redisUtil == null) {
                getRedisUtil();//获取bean
            }

            //拼接key
            String idm = this.id + "." + tenantId;
            try {
                //将key加密后存入
                redisUtil.hset(idm, this.MD5Encrypt(key), value, 86400);
            } catch (Exception e) {
                log.error("存入缓存失败！");
                e.printStackTrace();
            } finally {
                //释放锁
                readWriteLock.writeLock().unlock();
            }
        }

    }

    @Override
    public Object getObject(Object key) {
        log.info("获取缓存");


        String tenantId = (String) GlobalThreadLocalUtil.get("tenantId");
        if (!StringUtils.isEmpty(tenantId)) {
            //读锁开始
            readWriteLock.readLock().lock();
            if (redisUtil == null) {
                getRedisUtil();//获取bean
            }

            try {
                if (key != null) {
                    //String tenantId = (String) request.getSession().getAttribute("tenantId");

                    String idm = this.id + "." + tenantId;

                    return redisUtil.hget(idm, this.MD5Encrypt(key));
                }
            } catch (Exception e) {
                log.error("获取缓存失败！");
                e.printStackTrace();
            } finally {
                //释放读锁
                readWriteLock.readLock().unlock();
            }
        }

        return null;
    }

    @Override
    public Object removeObject(Object key) {
        log.info("删除缓存");

        readWriteLock.writeLock().lock();
        if (redisUtil == null) {
            getRedisUtil();//获取bean
        }


        try {
            if (key != null) {
                redisUtil.del(this.MD5Encrypt(key));
            }
        } catch (Exception e) {
            log.error("删除缓存失败！");
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }

        return null;
    }

    @Override
    public void clear() {
        String tenantId = (String) GlobalThreadLocalUtil.get("tenantId");
        if (!StringUtils.isEmpty(tenantId)) {
            log.info("清空缓存");
            if (redisUtil == null) {
                getRedisUtil();//获取bean
            }

            try {
                //String tenantId = (String) request.getSession().getAttribute("tenantId");

                String idm = this.id + "." + tenantId;
                redisUtil.del(idm);
            } catch (Exception e) {
                log.error("清空缓存失败！");
                e.printStackTrace();
            }
        }


    }

    @Override
    public int getSize() {
        if (redisUtil == null) {
            getRedisUtil();//获取bean
        }
        Long size = (Long) redisUtil.execute((RedisCallback<Long>) RedisServerCommands::dbSize);
        return size.intValue();

    }


    /**
     * MD5加密存储key,以节约内存
     */
    private String MD5Encrypt(Object key) {
        return DigestUtils.md5DigestAsHex(key.toString().getBytes());
    }

}
