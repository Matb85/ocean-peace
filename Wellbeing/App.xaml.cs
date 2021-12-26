using Microsoft.Maui;
using Microsoft.Maui.Controls;
using Microsoft.Maui.Controls.PlatformConfiguration.WindowsSpecific;
using Application = Microsoft.Maui.Controls.Application;
using System.Threading;

using Wellbeing.Models;
using Wellbeing.Views;

namespace Wellbeing
{
	public partial class App : Application
	{
		public App()
		{
			InitializeComponent();

			MainPage = new ApplicationsActivityPage();

			AppTimer appTimer = new AppTimer();
		}

        

		
    }
}
