#![cfg_attr(
  all(not(debug_assertions), target_os = "windows"),
  windows_subsystem = "windows"
)]

mod mayo;

use tauri::Manager;
// the payload type must implement `Serialize` and `Clone`.
#[derive(Clone, serde::Serialize)]
struct Payload {
  message: String,
}

fn main() {

  mayo::run_process();

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



#[tauri::command]
fn test_command(mut i: i8) -> i8{
  i=i+1;
  i.into()
}



