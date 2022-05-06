use sysinfo::{Pid, ProcessExt, System, SystemExt};


pub fn run_process() {
    let s = System::new();
    if let Some(process) = s.process(Pid::from(1337)) {
        println!("{:?}", process.pid());
    }
}