//
//  InputEventViewController.m
//  Test
//
//  Created by Venkatesh Arjunan on 10/7/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "InputEventViewController.h"
#import "TestAppDelegate.h"

@implementation ContactData

@end


@interface InputEventViewController ()
@property (weak, nonatomic) IBOutlet UITextField *points;
@property (weak, nonatomic) IBOutlet UITextField *notes;
@property (weak, nonatomic) IBOutlet UITextField *date;
@property (weak, nonatomic) IBOutlet UISwitch *reminderYearly;
@property (weak, nonatomic) IBOutlet UISwitch *reminderMonthly;
@property (weak, nonatomic) IBOutlet UISwitch *reminderWeekly;
- (IBAction)toggleYearly:(id)sender;
- (IBAction)toggleMonthly:(id)sender;
- (IBAction)toggleWeekly:(id)sender;
- (IBAction)cancel:(id)sender;
- (IBAction)save:(id)sender;
@end

@implementation InputEventViewController

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

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// this is needed to dismiss the keyboard which comes while typing

- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    
    if (theTextField == self.points) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.notes) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.points) {
        [theTextField resignFirstResponder];
    }
    return YES;
}


- (IBAction)save:(id)sender {

    if ( self.contactData == nil) {
        self.contactData = [ContactData alloc];
    }
    if ( self.selectedPerson != nil ) {
        self.contactData.selectedPerson = self.selectedPerson;
    }
    
    self.contactData.notes = self.notes.text;
    self.contactData.points = self.points.text;

    //for testing 
    self.contactData.reminderFreq = @"W";
//    self.contactData.date = self.date.text;
    
    // convert string to date
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    [df setDateFormat:@"yyyy-MM-dd hh:mm:ss a"];
    self.contactData.date = [df dateFromString: self.date.text];
    
    TestAppDelegate* appDelegate = (TestAppDelegate *) [[UIApplication sharedApplication] delegate];
    [appDelegate saveDocument:self.contactData];
    
    [self.navigationController popToRootViewControllerAnimated:YES];
    //[ self performSegueWithIdentifier:@"BackToInitialScreen" sender: self];
}


- (IBAction)toggleYearly:(id)sender {
    if (self.reminderYearly.on) {
        self.reminderWeekly.on = false;
        self.reminderMonthly.on = false;
    }
}

- (IBAction)toggleMonthly:(id)sender {
    if (self.reminderMonthly.on) {
        self.reminderWeekly.on = false;
        self.reminderYearly.on = false;
    }
}

- (IBAction)toggleWeekly:(id)sender {
    if (self.reminderWeekly.on) {
        self.reminderYearly.on = false;
        self.reminderMonthly.on = false;
    }
}

- (IBAction)cancel:(id)sender {
   // [ self performSegueWithIdentifier:@"BackToInitialScreen" sender: self];
   [self.navigationController popToRootViewControllerAnimated:YES];

}



@end
