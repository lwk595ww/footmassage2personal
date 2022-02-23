package com.seetech.footmassage2.core.configration.redisConfig;


import com.seetech.footmassage2.core.util.RedisUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching //默认将session放入到redis中 目的做session共享
@PropertySource("classpath:redis2.properties")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7200)//单元测试时，需注释掉此行配置,否则会报错
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.database}")
    private int database;

    private int redissionDatabase = 10;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.lettuce.pool.maxIdle}")
    private int poolMaxIdle;

    @Value("${spring.redis.lettuce.pool.minIdle}")
    private int poolMinIdle;

    @Value("${spring.redis.lettuce.pool.maxTotal}")
    private int maxTotal;

    @Value("${spring.redis.lettuce.pool.maxWait}")
    private int maxWait;

    @Resource(name = "taskExecutor")
    private TaskExecutor taskExecutor;

    @Bean
    public JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setMinIdle(poolMinIdle);
        poolConfig.setMaxIdle(poolMaxIdle);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPassword(RedisPassword.of(password));
        configuration.setPort(port);
        configuration.setDatabase(database);
        return configuration;
    }

    @Bean
    public JedisClientConfiguration clientConfiguration(JedisPoolConfig jedisPoolConfig) {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        return builder.usePooling()
                .poolConfig(jedisPoolConfig)
                .build();
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration, JedisClientConfiguration jedisClientConfiguration) {
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }


    @Bean
    public JedisShardInfo jedisShardInfo() {
        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port, timeout);
        jedisShardInfo.setPassword(password);
        return jedisShardInfo;
    }

    @Bean
    public ShardedJedisPool shardedJedisPool(JedisPoolConfig jedisPoolConfig, JedisShardInfo jedisShardInfo) {
//        GenericObjectPoolConfig<ShardedJedis> poolConfig = new GenericObjectPoolConfig<>();
//        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(host);
//        List<JedisShardInfo> jedisShardInfos = new ArrayList<>();
//        jedisShardInfos.add(jedisShardInfo1);
//        ShardedJedis shardedJedis = new ShardedJedis(jedisShardInfos);

        return new ShardedJedisPool(new GenericObjectPoolConfig<>(), Arrays.asList(jedisShardInfo));
    }


    @Bean(name = "jedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 使用 Jackson2JsonRedisSerializer 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(MyRedisJacksonConfig.getInstance());

        // 设置value的序列化规则和key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
//        redisTemplate.setEnableTransactionSupport(true);        // explicitly enable transaction support


        return redisTemplate;
    }

    @Bean("redisUtil")
    public RedisUtil redisUtil(@Qualifier("jedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return new RedisUtil(redisTemplate);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                this.getRedisCacheConfigurationWithTtl(60 * 10),// 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationMap()// 指定 key 策略
        );
    }


    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("System:DataRefresh");
    }


    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        //自定义设置缓存时间
//        redisCacheConfigurationMap.put("userCache", this.getRedisCacheConfigurationWithTtl(60));
        redisCacheConfigurationMap.put("WxTokenCache", this.getRedisCacheConfigurationWithTtl(60 * 115));
        redisCacheConfigurationMap.put("TenantInitRolesCache", this.getRedisCacheConfigurationWithTtl(60 * 60 * 12));
        redisCacheConfigurationMap.put("AppProductCache", this.getRedisCacheConfigurationWithTtl(60 * 60 * 12));
        redisCacheConfigurationMap.put("ArrangeDueueCache", this.getRedisCacheConfigurationWithTtl(60 * 60 * 12));
        redisCacheConfigurationMap.put("AppUserStaffCache", this.getRedisCacheConfigurationWithTtl(60 * 60 * 24 * 7));
        redisCacheConfigurationMap.put("CleanToiletRoom", this.getRedisCacheConfigurationWithTtl(60 * 60 * 24 * 7));

        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        jackson2JsonRedisSerializer.setObjectMapper(MyRedisJacksonConfig.getInstance());

        // 配置序列化
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .entryTtl(Duration.ofSeconds(seconds));
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new MyRedisKeyGenerator();
    }

    @Bean(name = "redissonClient")//redission
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port)
                .setDatabase(redissionDatabase)
                .setPassword(password);

//                .setConnectionPoolSize(2);
        config.setThreads(2);//目前仅用于分布式锁,不需要创建大线程池
        return Redisson.create(config);
    }

    @Bean//处理redis session 错误
    public CookieSerializer httpSessionIdResolver() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();

        // 源码默认为Lax
        // private String sameSite = "Lax";
        cookieSerializer.setSameSite(null);
        return cookieSerializer;
    }


}
