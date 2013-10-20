//
//  SelectPersonViewController.m
//  Test
//
//  Created by Venkatesh Arjunan on 10/12/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "SelectPersonViewController.h"
#import "InputEventViewController.h"
#import "Person.h"

@interface SelectPersonViewController ()
@property (weak, nonatomic) IBOutlet UIPickerView *personPicker;
@property (weak, nonatomic) Person* selectedPerson;
@property (weak, nonatomic) IBOutlet UITextField *firstName;
@property (weak, nonatomic) IBOutlet UITextField *lastName;
- (IBAction)goToEvent:(id)sender;

@end

@implementation SelectPersonViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
  
 /* for resizing the picker control
    CGSize pickerSize = [self.personPicker sizeThatFits:CGSizeZero];
    
    UIView *pickerTransformView = [[UIView alloc] initWithFrame:CGRectMake(0.0f, 0.0f, pickerSize.width, pickerSize.height)];
    pickerTransformView.transform = CGAffineTransformMakeScale(0.85f, 0.85f);
    
    [pickerTransformView addSubview:self.personPicker];
    [self.view addSubview:pickerTransformView];
    */
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ( [[segue identifier] isEqualToString:@"GoToEvent"] )
    {
        InputEventViewController * destController = [segue destinationViewController];
        [destController setTitle:@"Enter Event Details"];
        [destController setSelectedPerson:self.selectedPerson];
    }
}

#pragma mark Picker DataSource/Delegate

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return [self.existingPersons count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    Person * person = (Person *)[self.existingPersons objectAtIndex:row];
    return [NSString stringWithFormat:@"%@  %@", person.firstName ,person.lastName]  ;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    self.selectedPerson = [self.existingPersons objectAtIndex:row] ;
    self.lastName.text  = self.selectedPerson.lastName;
    self.firstName.text = self.selectedPerson.firstName ;
}


- (IBAction)goToEvent:(id)sender {
    [self performSegueWithIdentifier: @"GoToEvent" sender: self];
}
@end
