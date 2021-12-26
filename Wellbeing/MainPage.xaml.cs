using System;
using Microsoft.Maui.Controls;
using Microsoft.Maui.Essentials;

namespace Wellbeing
{
	public partial class MainPage : ContentPage
	{
		int count = 0;

		public MainPage()
		{
			InitializeComponent();
		}

		private void OnCounterClicked(object sender, EventArgs e)
		{
			count+=2*4;
			CounterLabel.Text = $"O kurwa działa: {count}";

			SemanticScreenReader.Announce(CounterLabel.Text);

			
		}
	}
}
