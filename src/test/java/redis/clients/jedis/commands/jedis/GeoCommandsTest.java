package redis.clients.jedis.commands.jedis;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static redis.clients.jedis.util.AssertUtil.assertByteArrayListEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;
import redis.clients.jedis.params.GeoAddParams;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.GeoRadiusStoreParam;
import redis.clients.jedis.util.SafeEncoder;

public class GeoCommandsTest extends JedisCommandsTestBase {
  final byte[] bfoo = { 0x01, 0x02, 0x03, 0x04 };
  final byte[] bA = { 0x0A };
  final byte[] bB = { 0x0B };
  final byte[] bC = { 0x0C };
  final byte[] bD = { 0x0D };
  final byte[] bNotexist = { 0x0F };

  private static final double EPSILON = 1e-5;

  @Test
  public void geoadd() {
    assertEquals(1, jedis.geoadd("foo", 1, 2, "a"));
    assertEquals(0, jedis.geoadd("foo", 2, 3, "a"));

    Map<String, GeoCoordinate> coordinateMap = new HashMap<>();
    coordinateMap.put("a", new GeoCoordinate(3, 4));
    coordinateMap.put("b", new GeoCoordinate(2, 3));
    coordinateMap.put("c", new GeoCoordinate(3.314, 2.3241));

    assertEquals(2, jedis.geoadd("foo", coordinateMap));

    // binary
    assertEquals(1, jedis.geoadd(bfoo, 1, 2, bA));
    assertEquals(0, jedis.geoadd(bfoo, 2, 3, bA));

    Map<byte[], GeoCoordinate> bcoordinateMap = new HashMap<>();
    bcoordinateMap.put(bA, new GeoCoordinate(3, 4));
    bcoordinateMap.put(bB, new GeoCoordinate(2, 3));
    bcoordinateMap.put(bC, new GeoCoordinate(3.314, 2.3241));

    assertEquals(2, jedis.geoadd(bfoo, bcoordinateMap));
  }

  @Test
  public void geoaddWithParams() {
    assertEquals(1, jedis.geoadd("foo", 1, 2, "a"));

    Map<String, GeoCoordinate> coordinateMap = new HashMap<>();
    coordinateMap.put("a", new GeoCoordinate(3, 4));
    assertEquals(0, jedis.geoadd("foo", GeoAddParams.geoAddParams().nx(), coordinateMap));
    assertEquals(1, jedis.geoadd("foo", GeoAddParams.geoAddParams().xx().ch(), coordinateMap));

    coordinateMap.clear();
    coordinateMap.put("b", new GeoCoordinate(6, 7));
    // never add elements.
    assertEquals(0, jedis.geoadd("foo", GeoAddParams.geoAddParams().xx(), coordinateMap));
    assertEquals(1, jedis.geoadd("foo", GeoAddParams.geoAddParams().nx(), coordinateMap));

    // binary
    assertEquals(1, jedis.geoadd(bfoo, 1, 2, bA));

    Map<byte[], GeoCoordinate> bcoordinateMap = new HashMap<>();
    bcoordinateMap.put(bA, new GeoCoordinate(3, 4));
    assertEquals(0, jedis.geoadd(bfoo, GeoAddParams.geoAddParams().nx(), bcoordinateMap));
    assertEquals(1, jedis.geoadd(bfoo, GeoAddParams.geoAddParams().xx().ch(), bcoordinateMap));

    bcoordinateMap.clear();
    bcoordinateMap.put(bB, new GeoCoordinate(6, 7));
    // never add elements.
    assertEquals(0, jedis.geoadd(bfoo, GeoAddParams.geoAddParams().xx(), bcoordinateMap));
    assertEquals(1, jedis.geoadd(bfoo, GeoAddParams.geoAddParams().nx(), bcoordinateMap));
  }

  @Test
  public void geodist() {
    prepareGeoData();

    Double dist = jedis.geodist("foo", "a", "b");
    assertEquals(157149, dist.intValue());

    dist = jedis.geodist("foo", "a", "b", GeoUnit.KM);
    assertEquals(157, dist.intValue());

    dist = jedis.geodist("foo", "a", "b", GeoUnit.MI);
    assertEquals(97, dist.intValue());

    dist = jedis.geodist("foo", "a", "b", GeoUnit.FT);
    assertEquals(515583, dist.intValue());

    // binary
    dist = jedis.geodist(bfoo, bA, bB);
    assertEquals(157149, dist.intValue());

    dist = jedis.geodist(bfoo, bA, bB, GeoUnit.KM);
    assertEquals(157, dist.intValue());

    dist = jedis.geodist(bfoo, bA, bB, GeoUnit.MI);
    assertEquals(97, dist.intValue());

    dist = jedis.geodist(bfoo, bA, bB, GeoUnit.FT);
    assertEquals(515583, dist.intValue());
  }

  @Test
  public void geohash() {
    prepareGeoData();

    List<String> hashes = jedis.geohash("foo", "a", "b", "notexist");
    assertEquals(3, hashes.size());
    assertEquals("s0dnu20t9j0", hashes.get(0));
    assertEquals("s093jd0k720", hashes.get(1));
    assertNull(hashes.get(2));

    // binary
    List<byte[]> bhashes = jedis.geohash(bfoo, bA, bB, bNotexist);
    assertEquals(3, bhashes.size());
    assertArrayEquals(SafeEncoder.encode("s0dnu20t9j0"), bhashes.get(0));
    assertArrayEquals(SafeEncoder.encode("s093jd0k720"), bhashes.get(1));
    assertNull(bhashes.get(2));
  }

  @Test
  public void geopos() {
    prepareGeoData();

    List<GeoCoordinate> coordinates = jedis.geopos("foo", "a", "b", "notexist");
    assertEquals(3, coordinates.size());
    assertEquals(3.0, coordinates.get(0).getLongitude(), EPSILON);
    assertEquals(4.0, coordinates.get(0).getLatitude(), EPSILON);
    assertEquals(2.0, coordinates.get(1).getLongitude(), EPSILON);
    assertEquals(3.0, coordinates.get(1).getLatitude(), EPSILON);
    assertNull(coordinates.get(2));

    List<GeoCoordinate> bcoordinates = jedis.geopos(bfoo, bA, bB, bNotexist);
    assertEquals(3, bcoordinates.size());
    assertEquals(3.0, bcoordinates.get(0).getLongitude(), EPSILON);
    assertEquals(4.0, bcoordinates.get(0).getLatitude(), EPSILON);
    assertEquals(2.0, bcoordinates.get(1).getLongitude(), EPSILON);
    assertEquals(3.0, bcoordinates.get(1).getLatitude(), EPSILON);
    assertNull(bcoordinates.get(2));
  }

  @Test
  public void georadius() {
    // prepare datas
    Map<String, GeoCoordinate> coordinateMap = new HashMap<>();
    coordinateMap.put("Palermo", new GeoCoordinate(13.361389, 38.115556));
    coordinateMap.put("Catania", new GeoCoordinate(15.087269, 37.502669));
    jedis.geoadd("Sicily", coordinateMap);

    List<GeoRadiusResponse> members = jedis.georadius("Sicily", 15, 37, 200, GeoUnit.KM);
    assertEquals(2, members.size());

    // sort
    members = jedis.georadius("Sicily", 15, 37, 200, GeoUnit.KM, GeoRadiusParam.geoRadiusParam()
        .sortAscending());
    assertEquals(2, members.size());
    assertEquals("Catania", members.get(0).getMemberByString());
    assertEquals("Palermo", members.get(1).getMemberByString());

    // sort, count 1
    members = jedis.georadius("Sicily", 15, 37, 200, GeoUnit.KM, GeoRadiusParam.geoRadiusParam()
        .sortAscending().count(1));
    assertEquals(1, members.size());

    // sort, count 1, withdist, withcoord
    members = jedis.georadius("Sicily", 15, 37, 200, GeoUnit.KM, GeoRadiusParam.geoRadiusParam()
        .sortAscending().count(1).withCoord().withDist().withHash());
    assertEquals(1, members.size());
    GeoRadiusResponse response = members.get(0);
    assertEquals(56.4413, response.getDistance(), EPSILON);
    assertEquals(15.087269, response.getCoordinate().getLongitude(), EPSILON);
    assertEquals(37.502669, response.getCoordinate().getLatitude(), EPSILON);
    assertEquals(3479447370796909L, response.getRawScore());

    // sort, count 1, with hash
    members = jedis.georadius("Sicily", 15, 37, 200, GeoUnit.KM, GeoRadiusParam.geoRadiusParam()
        .sortAscending().count(1).withHash());
    assertEquals(1, members.size());
    response = members.get(0);
    assertEquals(3479447370796909L, response.getRawScore());
  }

  @Test
  public void georadiusStore() {
    // prepare datas
    Map<String, GeoCoordinate> coordinateMap = new HashMap<>();
    coordinateMap.put("Palermo", new GeoCoordinate(13.361389, 38.115556));
    coordinateMap.put("Catania", new GeoCoordinate(15.087269, 37.502669));
    jedis.geoadd("Sicily", coordinateMap);

    long size = jedis.georadiusStore("Sicily", 15, 37, 200, GeoUnit.KM,
      GeoRadiusParam.geoRadiusParam(),
      GeoRadiusStoreParam.geoRadiusStoreParam().store("SicilyStore"));
    assertEquals(2, size);
    List<String> expected = new ArrayList<>();
    expected.add("Palermo");
    expected.add("Catania");
    assertEquals(expected, jedis.zrange("SicilyStore", 0, -1));
  }

  @Test
  public void georadiusReadonly() {
    // prepare datas
    Map<String, GeoCoordinate> coordinateMap = new HashMap<>();
    coordinateMap.put("Palermo", new GeoCoordinate(13.361389, 38.115556));
    coordinateMap.put("Catania", new GeoCoordinate(15.087269, 37.502669));
    jedis.geoadd("Sicily", coordinateMap);

    List<GeoRadiusResponse> members = jedis.georadiusReadonly("Sicily", 15, 37, 200, GeoUnit.KM);
    assertEquals(2, members.size());

    // sort
    members = jedis.georadiusReadonly("Sicily", 15, 37, 200, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending());
    assertEquals(2, members.size());
    assertEquals("Catania", members.get(0).getMemberByString());
    assertEquals("Palermo", members.get(1).getMemberByString());

    // sort, count 1
    members = jedis.georadiusReadonly("Sicily", 15, 37, 200, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending().count(1));
    assertEquals(1, members.size());

    // sort, count 1, withdist, withcoord
    members = jedis.georadiusReadonly("Sicily", 15, 37, 200, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending().count(1).withCoord().withDist());
    assertEquals(1, members.size());
    GeoRadiusResponse response = members.get(0);
    assertEquals(56.4413, response.getDistance(), EPSILON);
    assertEquals(15.087269, response.getCoordinate().getLongitude(), EPSILON);
    assertEquals(37.502669, response.getCoordinate().getLatitude(), EPSILON);
  }

  @Test
  public void georadiusBinary() {
    // prepare datas
    Map<byte[], GeoCoordinate> bcoordinateMap = new HashMap<>();
    bcoordinateMap.put(bA, new GeoCoordinate(13.361389, 38.115556));
    bcoordinateMap.put(bB, new GeoCoordinate(15.087269, 37.502669));
    jedis.geoadd(bfoo, bcoordinateMap);

    List<GeoRadiusResponse> members = jedis.georadius(bfoo, 15, 37, 200, GeoUnit.KM);
    assertEquals(2, members.size());

    // sort
    members = jedis.georadius(bfoo, 15, 37, 200, GeoUnit.KM, GeoRadiusParam.geoRadiusParam()
        .sortAscending());
    assertEquals(2, members.size());
    assertArrayEquals(bB, members.get(0).getMember());
    assertArrayEquals(bA, members.get(1).getMember());

    // sort, count 1
    members = jedis.georadius(bfoo, 15, 37, 200, GeoUnit.KM, GeoRadiusParam.geoRadiusParam()
        .sortAscending().count(1));
    assertEquals(1, members.size());

    // sort, count 1, withdist, withcoord
    members = jedis.georadius(bfoo, 15, 37, 200, GeoUnit.KM, GeoRadiusParam.geoRadiusParam()
        .sortAscending().count(1).withCoord().withDist());
    assertEquals(1, members.size());
    GeoRadiusResponse response = members.get(0);
    assertEquals(56.4413, response.getDistance(), EPSILON);
    assertEquals(15.087269, response.getCoordinate().getLongitude(), EPSILON);
    assertEquals(37.502669, response.getCoordinate().getLatitude(), EPSILON);
  }

  @Test
  public void georadiusStoreBinary() {
    // prepare datas
    Map<byte[], GeoCoordinate> bcoordinateMap = new HashMap<>();
    bcoordinateMap.put(bA, new GeoCoordinate(13.361389, 38.115556));
    bcoordinateMap.put(bB, new GeoCoordinate(15.087269, 37.502669));
    jedis.geoadd(bfoo, bcoordinateMap);

    long size = jedis.georadiusStore(bfoo, 15, 37, 200, GeoUnit.KM,
      GeoRadiusParam.geoRadiusParam(),
      GeoRadiusStoreParam.geoRadiusStoreParam().store("SicilyStore"));
    assertEquals(2, size);
    List<byte[]> bexpected = new ArrayList<>();
    bexpected.add(bA);
    bexpected.add(bB);
    assertByteArrayListEquals(bexpected, jedis.zrange("SicilyStore".getBytes(), 0, -1));
  }

  @Test
  public void georadiusReadonlyBinary() {
    // prepare datas
    Map<byte[], GeoCoordinate> bcoordinateMap = new HashMap<>();
    bcoordinateMap.put(bA, new GeoCoordinate(13.361389, 38.115556));
    bcoordinateMap.put(bB, new GeoCoordinate(15.087269, 37.502669));
    jedis.geoadd(bfoo, bcoordinateMap);

    List<GeoRadiusResponse> members = jedis.georadiusReadonly(bfoo, 15, 37, 200, GeoUnit.KM);
    assertEquals(2, members.size());

    // sort
    members = jedis.georadiusReadonly(bfoo, 15, 37, 200, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending());
    assertEquals(2, members.size());
    assertArrayEquals(bB, members.get(0).getMember());
    assertArrayEquals(bA, members.get(1).getMember());

    // sort, count 1
    members = jedis.georadiusReadonly(bfoo, 15, 37, 200, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending().count(1));
    assertEquals(1, members.size());

    // sort, count 1, withdist, withcoord
    members = jedis.georadiusReadonly(bfoo, 15, 37, 200, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending().count(1).withCoord().withDist());
    assertEquals(1, members.size());
    GeoRadiusResponse response = members.get(0);
    assertEquals(56.4413, response.getDistance(), EPSILON);
    assertEquals(15.087269, response.getCoordinate().getLongitude(), EPSILON);
    assertEquals(37.502669, response.getCoordinate().getLatitude(), EPSILON);
  }

  @Test
  public void georadiusByMember() {
    jedis.geoadd("Sicily", 13.583333, 37.316667, "Agrigento");
    jedis.geoadd("Sicily", 13.361389, 38.115556, "Palermo");
    jedis.geoadd("Sicily", 15.087269, 37.502669, "Catania");

    List<GeoRadiusResponse> members = jedis.georadiusByMember("Sicily", "Agrigento", 100,
      GeoUnit.KM);
    assertEquals(2, members.size());

    members = jedis.georadiusByMember("Sicily", "Agrigento", 100, GeoUnit.KM, GeoRadiusParam
        .geoRadiusParam().sortAscending());
    assertEquals(2, members.size());
    assertEquals("Agrigento", members.get(0).getMemberByString());
    assertEquals("Palermo", members.get(1).getMemberByString());

    members = jedis.georadiusByMember("Sicily", "Agrigento", 100, GeoUnit.KM, GeoRadiusParam
        .geoRadiusParam().sortAscending().count(1).withCoord().withDist());
    assertEquals(1, members.size());

    GeoRadiusResponse member = members.get(0);
    assertEquals("Agrigento", member.getMemberByString());
    assertEquals(0, member.getDistance(), EPSILON);
    assertEquals(13.583333, member.getCoordinate().getLongitude(), EPSILON);
    assertEquals(37.316667, member.getCoordinate().getLatitude(), EPSILON);
  }

  @Test
  public void georadiusByMemberStore() {
    jedis.geoadd("Sicily", 13.583333, 37.316667, "Agrigento");
    jedis.geoadd("Sicily", 13.361389, 38.115556, "Palermo");
    jedis.geoadd("Sicily", 15.087269, 37.502669, "Catania");

    long size = jedis.georadiusByMemberStore("Sicily", "Agrigento", 100, GeoUnit.KM,
      GeoRadiusParam.geoRadiusParam(),
      GeoRadiusStoreParam.geoRadiusStoreParam().store("SicilyStore"));
    assertEquals(2, size);
    List<String> expected = new ArrayList<>();
    expected.add("Agrigento");
    expected.add("Palermo");
    assertEquals(expected, jedis.zrange("SicilyStore", 0, -1));
  }

  @Test
  public void georadiusByMemberReadonly() {
    jedis.geoadd("Sicily", 13.583333, 37.316667, "Agrigento");
    jedis.geoadd("Sicily", 13.361389, 38.115556, "Palermo");
    jedis.geoadd("Sicily", 15.087269, 37.502669, "Catania");

    List<GeoRadiusResponse> members = jedis.georadiusByMemberReadonly("Sicily", "Agrigento", 100,
      GeoUnit.KM);
    assertEquals(2, members.size());

    members = jedis.georadiusByMemberReadonly("Sicily", "Agrigento", 100, GeoUnit.KM,
      GeoRadiusParam.geoRadiusParam().sortAscending());
    assertEquals(2, members.size());
    assertEquals("Agrigento", members.get(0).getMemberByString());
    assertEquals("Palermo", members.get(1).getMemberByString());

    members = jedis.georadiusByMemberReadonly("Sicily", "Agrigento", 100, GeoUnit.KM,
      GeoRadiusParam.geoRadiusParam().sortAscending().count(1).withCoord().withDist());
    assertEquals(1, members.size());

    GeoRadiusResponse member = members.get(0);
    assertEquals("Agrigento", member.getMemberByString());
    assertEquals(0, member.getDistance(), EPSILON);
    assertEquals(13.583333, member.getCoordinate().getLongitude(), EPSILON);
    assertEquals(37.316667, member.getCoordinate().getLatitude(), EPSILON);
  }

  @Test
  public void georadiusByMemberBinary() {
    jedis.geoadd(bfoo, 13.583333, 37.316667, bA);
    jedis.geoadd(bfoo, 13.361389, 38.115556, bB);
    jedis.geoadd(bfoo, 15.087269, 37.502669, bC);

    List<GeoRadiusResponse> members = jedis.georadiusByMember(bfoo, bA, 100, GeoUnit.KM);
    assertEquals(2, members.size());

    members = jedis.georadiusByMember(bfoo, bA, 100, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending());
    assertEquals(2, members.size());
    assertArrayEquals(bA, members.get(0).getMember());
    assertArrayEquals(bB, members.get(1).getMember());

    members = jedis.georadiusByMember(bfoo, bA, 100, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending().count(1).withCoord().withDist());
    assertEquals(1, members.size());

    GeoRadiusResponse member = members.get(0);
    assertArrayEquals(bA, member.getMember());
    assertEquals(0, member.getDistance(), EPSILON);
    assertEquals(13.583333, member.getCoordinate().getLongitude(), EPSILON);
    assertEquals(37.316667, member.getCoordinate().getLatitude(), EPSILON);
  }

  @Test
  public void georadiusByMemberStoreBinary() {
    jedis.geoadd(bfoo, 13.583333, 37.316667, bA);
    jedis.geoadd(bfoo, 13.361389, 38.115556, bB);
    jedis.geoadd(bfoo, 15.087269, 37.502669, bC);

    assertEquals(2, jedis.georadiusByMemberStore(bfoo, bA, 100, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam(),
        GeoRadiusStoreParam.geoRadiusStoreParam().store("SicilyStore")));
    List<byte[]> bexpected = new ArrayList<>();
    bexpected.add(bA);
    bexpected.add(bB);
    assertByteArrayListEquals(bexpected, jedis.zrange("SicilyStore".getBytes(), 0, -1));
  }

  @Test
  public void georadiusByMemberReadonlyBinary() {
    jedis.geoadd(bfoo, 13.583333, 37.316667, bA);
    jedis.geoadd(bfoo, 13.361389, 38.115556, bB);
    jedis.geoadd(bfoo, 15.087269, 37.502669, bC);

    List<GeoRadiusResponse> members = jedis.georadiusByMemberReadonly(bfoo, bA, 100, GeoUnit.KM);
    assertEquals(2, members.size());

    members = jedis.georadiusByMemberReadonly(bfoo, bA, 100, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending());
    assertEquals(2, members.size());
    assertArrayEquals(bA, members.get(0).getMember());
    assertArrayEquals(bB, members.get(1).getMember());

    members = jedis.georadiusByMemberReadonly(bfoo, bA, 100, GeoUnit.KM,
        GeoRadiusParam.geoRadiusParam().sortAscending().count(1).withCoord().withDist());
    assertEquals(1, members.size());

    GeoRadiusResponse member = members.get(0);
    assertArrayEquals(bA, member.getMember());
    assertEquals(0, member.getDistance(), EPSILON);
    assertEquals(13.583333, member.getCoordinate().getLongitude(), EPSILON);
    assertEquals(37.316667, member.getCoordinate().getLatitude(), EPSILON);
  }

  private void prepareGeoData() {
    Map<String, GeoCoordinate> coordinateMap = new HashMap<>();
    coordinateMap.put("a", new GeoCoordinate(3, 4));
    coordinateMap.put("b", new GeoCoordinate(2, 3));
    coordinateMap.put("c", new GeoCoordinate(3.314, 2.3241));

    assertEquals(3, jedis.geoadd("foo", coordinateMap));

    Map<byte[], GeoCoordinate> bcoordinateMap = new HashMap<>();
    bcoordinateMap.put(bA, new GeoCoordinate(3, 4));
    bcoordinateMap.put(bB, new GeoCoordinate(2, 3));
    bcoordinateMap.put(bC, new GeoCoordinate(3.314, 2.3241));

    assertEquals(3, jedis.geoadd(bfoo, bcoordinateMap));
  }
}
