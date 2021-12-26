using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


public struct appTime
{
    string appName;
    float runingTime;
}

namespace Wellbeing.Models
{
    public partial class UsedApps
    {
        private static UsedApps _instance;

        public static UsedApps GetInstance()
        {
            if (_instance == null)
            {
                _instance = new UsedApps();
            }

            return _instance;
        }


        public Dictionary<string, float> AppsDictionary = new Dictionary<string, float>()
        {
            {"Liga", 12312 },
        };

        //public List<appTime> appTimes = new List<appTime>();

        public void UsingApp(string app, float time)
        {
            if (app != null)
            {
                if (AppsDictionary.ContainsKey(app))
                {
                    AppsDictionary[app] += time;
                }
                else
                {
                    AppsDictionary.Add(app, 0);
                }
            }
        }
    }
}
