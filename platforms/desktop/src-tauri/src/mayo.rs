use sysinfo::{Pid, ProcessExt, System, SystemExt};
use std::{thread, time};
use active_win_pos_rs::{get_active_window};

pub fn run_process() {
    thread::spawn(move || {


        let mut sys = System::new_all();
        let mut prev_window_id: u64 = 0;

        sys.refresh_all();
        for (pid, process) in sys.processes() {
            println!("[{}] {}",pid, process.name());
        }

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
            sys.refresh_all();
            match get_active_window() {
                Ok(active_window) => {
                    if active_window.process_id != prev_window_id {
                        if let Some(process) = sys.process(Pid::from(active_window.process_id as i32)) {
                            println!("{}", process.name());
                        }
                        println!("active window: {:?}", active_window);
                    }
                    prev_window_id = active_window.process_id;
                },
                Err(()) => {
                    println!("error occurred while getting the active window");
                }
            }

            thread::sleep(time::Duration::from_millis(200));

        }
    });
}