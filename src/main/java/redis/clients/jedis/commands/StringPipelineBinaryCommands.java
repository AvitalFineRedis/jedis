package redis.clients.jedis.commands;

import redis.clients.jedis.Response;
import redis.clients.jedis.args.BitOP;
import redis.clients.jedis.params.BitPosParams;
import redis.clients.jedis.params.GetExParams;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.StrAlgoLCSParams;
import redis.clients.jedis.resps.LCSMatchResult;

import java.util.List;

public interface StringPipelineBinaryCommands {

  Response<String> set(byte[] key, byte[] value);

  Response<String> set(byte[] key, byte[] value, SetParams params);

  Response<byte[]> get(byte[] key);

  Response<byte[]> getDel(byte[] key);

  Response<byte[]> getEx(byte[] key, GetExParams params);

  Response<Boolean> setbit(byte[] key, long offset, boolean value);

  Response<Boolean> getbit(byte[] key, long offset);

  Response<Long> setrange(byte[] key, long offset, byte[] value);

  Response<byte[]> getrange(byte[] key, long startOffset, long endOffset);

  Response<byte[]> getSet(byte[] key, byte[] value);

  Response<Long> setnx(byte[] key, byte[] value);

  Response<String> setex(byte[] key, long seconds, byte[] value);

  Response<String> psetex(byte[] key, long milliseconds, byte[] value);

  Response<List<byte[]>> mget(byte[]... keys);

  Response<String> mset(byte[]... keysvalues);

  Response<Long> msetnx(byte[]... keysvalues);

  Response<Long> incr(byte[] key);

  Response<Long> incrBy(byte[] key, long increment);

  Response<Double> incrByFloat(byte[] key, double increment);

  Response<Long> decr(byte[] key);

  Response<Long> decrBy(byte[] key, long decrement);

  Response<Long> append(byte[] key, byte[] value);

  Response<byte[]> substr(byte[] key, int start, int end);

  Response<Long> strlen(byte[] key);

  Response<Long> bitcount(byte[] key);

  Response<Long> bitcount(byte[] key, long start, long end);

  Response<Long> bitpos(byte[] key, boolean value);

  Response<Long> bitpos(byte[] key, boolean value, BitPosParams params);

  Response<List<Long>> bitfield(byte[] key, byte[]... arguments);

  Response<List<Long>> bitfieldReadonly(byte[] key, byte[]... arguments);

  Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys);

  Response<LCSMatchResult> strAlgoLCSKeys(final byte[] keyA, final byte[] keyB, final StrAlgoLCSParams params);
}
