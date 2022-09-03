use serde::{Deserialize, Serialize};

pub use auth::*;
pub use dict::*;
pub use res::*;
pub use role::*;
pub use sign_in::*;
pub use user::*;

pub mod auth;
pub mod blog;
pub mod dict;
pub mod res;
pub mod role;
pub mod sign_in;
pub mod user;

#[derive(Serialize, Deserialize, Clone, Debug)]
pub struct EmptyDTO {}

/// IdDTO
#[derive(Serialize, Deserialize, Clone, Debug)]
pub struct IdDTO {
    pub id: Option<String>,
}
