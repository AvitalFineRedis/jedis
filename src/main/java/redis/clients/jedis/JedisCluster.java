package redis.clients.jedis;

import redis.clients.jedis.executors.ClusterCommandExecutor;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.util.Pool;

public class JedisCluster extends UnifiedJedis {

  /**
   * Default timeout in milliseconds.
   */
  public static final int DEFAULT_TIMEOUT = 2000;
  public static final int DEFAULT_MAX_ATTEMPTS = 5;

  public JedisCluster(HostAndPort node) {
    this(Collections.singleton(node));
  }

  public JedisCluster(HostAndPort node, int timeout) {
    this(Collections.singleton(node), timeout);
  }

  public JedisCluster(HostAndPort node, int timeout, int maxAttempts) {
    this(Collections.singleton(node), timeout, maxAttempts);
  }

  public JedisCluster(HostAndPort node, final GenericObjectPoolConfig<Connection> poolConfig) {
    this(Collections.singleton(node), poolConfig);
  }

  public JedisCluster(HostAndPort node, int timeout, final GenericObjectPoolConfig<Connection> poolConfig) {
    this(Collections.singleton(node), timeout, poolConfig);
  }

  public JedisCluster(HostAndPort node, int timeout, int maxAttempts,
      final GenericObjectPoolConfig<Connection> poolConfig) {
    this(Collections.singleton(node), timeout, maxAttempts, poolConfig);
  }

  public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts,
      final GenericObjectPoolConfig<Connection> poolConfig) {
    this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, poolConfig);
  }

  public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts,
      String password, final GenericObjectPoolConfig<Connection> poolConfig) {
    this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password,
        poolConfig);
  }

  public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts,
      String password, String clientName, final GenericObjectPoolConfig<Connection> poolConfig) {
    this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password,
        clientName, poolConfig);
  }

  public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts,
      String user, String password, String clientName,
      final GenericObjectPoolConfig<Connection> poolConfig) {
    this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, user, password,
        clientName, poolConfig);
  }

  public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts,
      String password, String clientName, final GenericObjectPoolConfig<Connection> poolConfig,
      boolean ssl) {
    this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password,
        clientName, poolConfig, ssl);
  }

  public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts,
      String user, String password, String clientName,
      final GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
    this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, user, password,
        clientName, poolConfig, ssl);
  }

  public JedisCluster(HostAndPort node, final JedisClientConfig clientConfig, int maxAttempts,
      final GenericObjectPoolConfig<Connection> poolConfig) {
    this(Collections.singleton(node), clientConfig, maxAttempts, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> nodes) {
    this(nodes, DEFAULT_TIMEOUT);
  }

  public JedisCluster(Set<HostAndPort> nodes, int timeout) {
    this(nodes, timeout, DEFAULT_MAX_ATTEMPTS);
  }

  public JedisCluster(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
    super(nodes, timeout, maxAttempts);
  }

  public JedisCluster(Set<HostAndPort> nodes, final GenericObjectPoolConfig<Connection> poolConfig) {
    this(nodes, DEFAULT_TIMEOUT, DEFAULT_MAX_ATTEMPTS, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> nodes, int timeout,
      final GenericObjectPoolConfig<Connection> poolConfig) {
    this(nodes, timeout, DEFAULT_MAX_ATTEMPTS, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts,
      final GenericObjectPoolConfig<Connection> poolConfig) {
    this(jedisClusterNode, timeout, timeout, maxAttempts, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout,
      int soTimeout, int maxAttempts, final GenericObjectPoolConfig<Connection> poolConfig) {
    this(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, null, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout,
      int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig<Connection> poolConfig) {
    this(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, null, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout,
      int soTimeout, int maxAttempts, String password, String clientName,
      GenericObjectPoolConfig<Connection> poolConfig) {
    this(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, null, password, clientName,
        poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout,
      int soTimeout, int maxAttempts, String user, String password, String clientName,
      GenericObjectPoolConfig<Connection> poolConfig) {
    this(jedisClusterNode, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
        .socketTimeoutMillis(soTimeout).user(user).password(password).clientName(clientName).build(),
        maxAttempts, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout,
      int soTimeout, int infiniteSoTimeout, int maxAttempts, String user, String password,
      String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
    this(jedisClusterNode, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
        .socketTimeoutMillis(soTimeout).blockingSocketTimeoutMillis(infiniteSoTimeout)
        .user(user).password(password).clientName(clientName).build(), maxAttempts, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout,
      int soTimeout, int maxAttempts, String password, String clientName,
      GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
    this(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, null, password, clientName,
        poolConfig, ssl);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout,
      int soTimeout, int maxAttempts, String user, String password, String clientName,
      GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
    this(jedisClusterNode, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
        .socketTimeoutMillis(soTimeout).user(user).password(password).clientName(clientName).ssl(ssl).build(),
        maxAttempts, poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, JedisClientConfig clientConfig,
      int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
    this(jedisClusterNode, clientConfig, maxAttempts,
        Duration.ofMillis((long) clientConfig.getSocketTimeoutMillis() * maxAttempts), poolConfig);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNode, JedisClientConfig clientConfig,
      int maxAttempts, Duration maxTotalRetriesDuration, GenericObjectPoolConfig<Connection> poolConfig) {
    super(jedisClusterNode, clientConfig, poolConfig, maxAttempts, maxTotalRetriesDuration);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNodes, JedisClientConfig clientConfig, int maxAttempts) {
    super(jedisClusterNodes, clientConfig, maxAttempts);
  }

  public JedisCluster(Set<HostAndPort> jedisClusterNodes, JedisClientConfig clientConfig, int maxAttempts, Duration maxTotalRetriesDuration) {
    super(jedisClusterNodes, clientConfig, maxAttempts, maxTotalRetriesDuration);
  }

  public Map<String, Pool<Connection>> getClusterNodes() {
    return ((ClusterCommandExecutor) executor).provider.getNodes();
  }
}
