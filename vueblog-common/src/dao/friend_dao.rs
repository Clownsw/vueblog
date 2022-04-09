use crate::pojo::friend::{InsertFriend, SelectFriend, UpdateFriend};
use sqlx::mysql::MySqlQueryResult;
use sqlx::MySqlPool;

/**
 * 查询所有友联
 */
pub async fn select_all(db_pool: &MySqlPool) -> Result<Vec<SelectFriend>, sqlx::Error> {
    sqlx::query_as!(
        SelectFriend,
        r#"
            SELECT * FROM m_friend
        "#
    )
    .fetch_all(db_pool)
    .await
}

/**
 * 添加一个友联
 */
pub async fn insert_friend(
    db_pool: &MySqlPool,
    insert_friend: InsertFriend,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            INSERT INTO m_friend(name, description, href, avatar)
            VALUES(?, ?, ?, ?)
        "#,
        insert_friend.name,
        insert_friend.description,
        insert_friend.href,
        insert_friend.avatar,
    )
    .execute(db_pool)
    .await
}

/**
 * 更新一个友联
 */
pub async fn update_friend(
    db_pool: &MySqlPool,
    update_friend: UpdateFriend,
) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            UPDATE m_friend SET name = ?, description = ?, href = ?, avatar = ? WHERE id = ?
        "#,
        update_friend.name,
        update_friend.description,
        update_friend.href,
        update_friend.avatar,
        update_friend.id,
    )
    .execute(db_pool)
    .await
}

/**
 * 删除一个友联通过ID
 */
pub async fn delete_by_id(db_pool: &MySqlPool, id: i64) -> Result<MySqlQueryResult, sqlx::Error> {
    sqlx::query!(
        r#"
            DELETE FROM m_friend WHERE id = ?
        "#,
        id
    )
    .execute(db_pool)
    .await
}
