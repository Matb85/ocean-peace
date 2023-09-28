using System.Diagnostics;

namespace ocean_peace.Components.Buttons;

public partial class Normal :Grid
{
	public static readonly BindableProperty LabelTextPropperty =
		BindableProperty.Create(nameof(LabelText), typeof(string), typeof(Normal), defaultValue: default(string), propertyChanged: OnTextChanged);

    private static void OnTextChanged(BindableObject bindable, object oldValue, object newValue)
    {
        var buttonView = (Normal)bindable;
        buttonView.Label.Text = (string)newValue;
    }

    public string LabelText 
	{ 
		get => (string)GetValue(LabelTextPropperty);
		set => SetValue(LabelTextPropperty, value); 
	}

	public Normal()
	{
		InitializeComponent();
    }

    private void Button_Pressed(object sender, EventArgs e)
    {
		Console.WriteLine("pressed");
    }
}