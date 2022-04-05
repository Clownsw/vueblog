use redis::{AsyncCommands, FromRedisValue, RedisError, ToRedisArgs};
use redis_async_pool::{deadpool::managed::Object, RedisConnection};

/**
 * 从redis中获取
 */
pub async fn get<'a, T: FromRedisValue, K: ToRedisArgs + Send + Sync + 'a>(
    conn: &mut Object<RedisConnection, RedisError>,
    k: K,
) -> Result<T, RedisError> {
    conn.get::<_, T>(k).await
}

/**
 * 将数据存在redis并设置ttl
 */
pub async fn set_and_ttl<'a, K, V>(
    conn: &mut Object<RedisConnection, RedisError>,
    k: K,
    v: V,
    ttl: usize,
) -> bool
where
    K: ToRedisArgs + Send + Sync + 'a,
    V: ToRedisArgs + Send + Sync + 'a,
{
    match conn.pset_ex::<_, _, ()>(k, v, ttl).await {
        Ok(_) => true,
        Err(_) => false,
    }
}

/**
 * 从redis中删除
 */
pub async fn delete<'a, K: ToRedisArgs + Send + Sync + 'a>(
    conn: &mut Object<RedisConnection, RedisError>,
    k: K,
) -> bool {
    match conn.del::<K, ()>(k).await {
        Ok(_) => true,
        Err(_) => false,
    }
}
