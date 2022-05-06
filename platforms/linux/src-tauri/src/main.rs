#![cfg_attr(
  all(not(debug_assertions), target_os = "windows"),
  windows_subsystem = "windows"
)]

mod mayo;
use sysinfo::{Pid, PidExt, ProcessExt, System, SystemExt};
use active_win_pos_rs::{get_active_window};

use tauri::Manager;
// the payload type must implement `Serialize` and `Clone`.
#[derive(Clone, serde::Serialize)]
struct Payload {
  message: String,
}

fn main() {
  use std::thread;
  
  thread::spawn(move || {
    //loop {
      let mut sys = System::new_all();
      sys.refresh_all();
      for (pid, process) in sys.processes() {
        println!("[{}] {}",pid, process.name());
      }

      let mut prev_window_id: u64 = 0;
      match get_active_window() {
        Ok(active_window) => {
            println!("active window: {:?}", active_window);

            prev_window_id = active_window.process_id;
        },
        Err(()) => {
            println!("error occurred while getting the active window");
        }
        
      }
      loop {
        match get_active_window() {
          Ok(active_window) => {
              if active_window.process_id != prev_window_id {
                println!("active window: {:?}", active_window);
              }
              prev_window_id = active_window.process_id;
          },
          Err(()) => {
              println!("error occurred while getting the active window");
          }
        }
      }
  });

  tauri::Builder::default()
    .setup(|app| {
      // listen to the `event-name` (emitted on any window)
      let id = app.listen_global("OnChangeView", |event| {
      //   let viewName: String = ["{:?}", &event.payload()];
      //   app.emit_all("ChangeView", Payload { message: viewName.into()}).unwrap();
      });
      // unlisten to the event using the `id` returned on the `listen_global` function
      // an `once_global` API is also exposed on the `App` struct
      app.unlisten(id);
      Ok(())
    })

    .invoke_handler(tauri::generate_handler![test_command])

    .run(tauri::generate_context!())
    .expect("failed to run app");
}

use serde::ser::{Serialize};
use serde::Deserialize;

#[tauri::command]
fn test_command(mut i: i8) -> i8{
  i=i+1;
  i.into()
}



