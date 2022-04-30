#![cfg_attr(
  all(not(debug_assertions), target_os = "windows"),
  windows_subsystem = "windows"
)]

fn main() {
  tauri::Builder::default()
    .invoke_handler(tauri::generate_handler![test_command])
    .run(tauri::generate_context!())
    .expect("error while running tauri application");
}

use serde::ser::{Serialize};
use serde::Deserialize;

#[tauri::command]
fn test_command(mut i: i8) -> i8{
  i=i+1;
  i.into()
}