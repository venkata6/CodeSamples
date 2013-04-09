//
//  ItemInputViewController.m
//  Locations
//
//  Created by Venkatesh Arjunan on 2/19/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "ItemInputViewController.h"

@interface ItemInputViewController ()
@end

@implementation ContactData

@synthesize firstName,lastName,initialPoints,notes,phoneNo,emailAddr,eventDate;

@end

@implementation ItemInputViewController

@synthesize contactData;
@synthesize delegate,datePickerView;


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

- (IBAction)saveContact:(id)sender {
    self.contactData = [ContactData alloc];
    self.contactData.firstName = self.firstName.text;
    [[self contactData] setLastName:  self.lastName.text];
    [[self contactData] setInitialPoints: self.initialPoints.text];
    self.contactData.notes = self.notes.text;

    if ([ [self delegate] respondsToSelector:@selector(didAddContact:)]) {
        [ [self delegate] didAddContact:self.contactData];
    }
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)cancel:(id)sender {
    
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

// this is needed to dismiss the keyboard which comes while typing

- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {

    if (theTextField == self.firstName) {
        [theTextField resignFirstResponder];
    }
   else if (theTextField == self.lastName) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.initialPoints) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.notes) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.emailAddr) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.eventDate) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.phoneNo) {
        [theTextField resignFirstResponder];
    }
    return YES;
}

- (IBAction)showPicker:(id)sender {
    ABPeoplePickerNavigationController *picker =
    [[ABPeoplePickerNavigationController alloc] init];
    picker.peoplePickerDelegate = self;
    [self presentViewController:picker animated:YES completion:nil];
}

- (void)peoplePickerNavigationControllerDidCancel:
(ABPeoplePickerNavigationController *)peoplePicker
{
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (BOOL)peoplePickerNavigationController:
(ABPeoplePickerNavigationController *)peoplePicker
      shouldContinueAfterSelectingPerson:(ABRecordRef)person {
    [self displayPerson:person];
    [self dismissViewControllerAnimated:YES completion:nil];
    return NO;
}
- (BOOL)peoplePickerNavigationController:
(ABPeoplePickerNavigationController *)peoplePicker
      shouldContinueAfterSelectingPerson:(ABRecordRef)person
                                property:(ABPropertyID)property
                              identifier:(ABMultiValueIdentifier)identifier
{
    return NO;
}

- (void)displayPerson:(ABRecordRef)person
{
    NSString* name = (__bridge_transfer NSString*)ABRecordCopyValue(person, kABPersonFirstNameProperty);
    self.firstName.text = name;
    name = (__bridge_transfer NSString*)ABRecordCopyValue(person,kABPersonLastNameProperty);
    self.lastName.text = name;
    
    NSString* phone = nil;
    ABMultiValueRef phoneNumbers = ABRecordCopyValue(person,kABPersonPhoneProperty);
    if (ABMultiValueGetCount(phoneNumbers) > 0) {
        phone = (__bridge_transfer NSString*)
        ABMultiValueCopyValueAtIndex(phoneNumbers, 0);
    } else {
        phone = @"[None]";
    }
    self.phoneNo.text = phone;
    CFRelease(phoneNumbers);


    NSString* email = nil;
    ABMultiValueRef emails = ABRecordCopyValue(person,kABPersonEmailProperty);
    if (ABMultiValueGetCount(emails) > 0) {
        email = (__bridge_transfer NSString*)
        ABMultiValueCopyValueAtIndex(emails, 0);
    } else {
        email = @"[None]";
    }
    self.emailAddr.text = email;
    CFRelease(emails);

}

- (IBAction)selectDate:(id)sender {

    //datePickerView = [[UIDatePicker alloc] initWithFrame:CGRectZero]; CGRectMake(0.0, 0.0, 320.0, 120.0)
    datePickerView = [[UIDatePicker alloc] initWithFrame:CGRectMake(0.0, 100.0, 160.0, 80.0)];
    datePickerView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleTopMargin;
    datePickerView.datePickerMode = UIDatePickerModeDate;
    
    
    datePickerView.hidden = NO;
    
    [self.view addSubview:datePickerView];

}
@end


