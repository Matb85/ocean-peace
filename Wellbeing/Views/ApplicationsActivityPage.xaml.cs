using System;
using System.Linq;
using Microsoft.Maui.Controls;
using Microsoft.Maui.Essentials;

using Wellbeing.Models;

namespace Wellbeing.Views
{
    public partial class ApplicationsActivityPage : ContentPage
    {
        public ApplicationsActivityPage()
        {
            InitializeComponent();



            Device.StartTimer(TimeSpan.FromMilliseconds(1000/100), () =>
            {
                MainThread.BeginInvokeOnMainThread(() =>
                {

                    AppsArray.Clear();

                    AppsArray.Children.Add(new Label { Text = "Today used apps:", HorizontalOptions = LayoutOptions.Center });


                    for (int i = 0; i < UsedApps.GetInstance().AppsDictionary.Count; i++)
                    {

                        float time = UsedApps.GetInstance().AppsDictionary.ElementAt(i).Value;

                        string timer = null;

                        timer += ((int)(time / 3600) / 10).ToString() + ((int)(time / 3600) % 10).ToString() +":";
                        timer += ((int)((time / 60) % 60)/ 10).ToString() + ((int)((time / 60) % 60)  % 10).ToString() +":";
                        timer += ((int)(time % 60) / 10).ToString() + ((int)(time % 60) % 10).ToString();


                        AppsArray.Children.Add(new Label { Text = UsedApps.GetInstance().AppsDictionary.ElementAt(i).Key + ":    " + timer});

                    }
                });
                return true;
            });

            

        }


        
    }
}