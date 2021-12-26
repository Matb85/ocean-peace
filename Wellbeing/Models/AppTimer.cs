using System;
using System.Runtime.InteropServices;
using System.Diagnostics;
using System.Text;
using System.Threading;
using Microsoft.Maui.Controls;

namespace Wellbeing.Models
{
    public class AppTimer
    {
        [DllImport("user32.dll")]
        static extern IntPtr GetForegroundWindow();

        [DllImport("user32.dll")]
        static extern int GetWindowText(IntPtr hWnd, StringBuilder text, int count);



        [DllImport("user32.dll")]
        public static extern IntPtr GetWindowThreadProcessId(IntPtr hWnd, out uint ProcessId);




        public AppTimer()
        {



            Device.StartTimer(TimeSpan.FromMilliseconds(100), () =>
            {

                ThreadPool.QueueUserWorkItem(o =>
                {
                    var sw = Stopwatch.StartNew();

                    string app = GetActiveWindow();

                    UsedApps.GetInstance().UsingApp(app, 1/10f);

                    sw.Stop();
                });

                return true;
            });

            
        }
            



        private string GetActiveWindow()
        {
            try
            {
                const int nChars = 256;
                IntPtr handle;
                StringBuilder Buff = new StringBuilder(nChars);
                handle = GetForegroundWindow();

                uint pID;

                string window = null;

                GetWindowThreadProcessId(handle, out pID);
                var p = Process.GetProcessById((int)pID);
                window = p.MainModule.FileName.ToString();

                return window;
            }
            catch
            {
                return null;
            }
        }
    }
}
