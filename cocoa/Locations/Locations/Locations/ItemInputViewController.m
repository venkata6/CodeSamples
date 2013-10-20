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
@synthesize delegate;
@synthesize keyBoardOffset;

UITextField* activeField=nil;


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
 	// Do any additional setup after loading the view.
    // load today's date as the default date 
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    df.dateStyle = NSDateFormatterMediumStyle;
    self.eventDate.text = [NSString stringWithFormat:@"%@",
                           [df stringFromDate:[NSDate date]]];
    
    // add a toolbar for datePicker 'done' button
    
    UIToolbar* keyboardDoneButtonView = [[UIToolbar alloc] init];
    keyboardDoneButtonView.barStyle = UIBarStyleBlack;
    keyboardDoneButtonView.translucent = YES;
    keyboardDoneButtonView.tintColor = nil;
    [keyboardDoneButtonView sizeToFit];
    UIBarButtonItem* doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"
                                                                   style:UIBarButtonItemStyleBordered target:self
                                                                  action:@selector(datePickerDoneClicked:)];
    
    [keyboardDoneButtonView setItems:[NSArray arrayWithObjects:doneButton, nil]];
    [self.view addSubview:keyboardDoneButtonView]; // top 'done' buttn

    
    [self.datePicker addTarget:self
                        action:@selector(LabelChange:)
              forControlEvents:UIControlEventValueChanged];
    
    self.sv.backgroundColor = [UIColor whiteColor];
    
    //register message
    [self registerForKeyboardNotifications];
    
    
    [super viewDidLoad];
 
}

- (IBAction)pickerDoneClicked:(id)sender {
    //[self.datePicker removeFromSuperview];
    self.datePicker.hidden = YES ;
    [self.eventDate resignFirstResponder];
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    
    if ( textField == self.eventDate)
    {
        self.datePicker.hidden = NO;

     
        return NO;
    }
    else
        return YES;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

- (IBAction)selectDate:(id)sender {
    
    [self.eventDate resignFirstResponder];

}
- (void)datePickerDoneClicked:(id)sender{
    //[self.datePicker removeFromSuperview];
    self.datePicker.hidden = YES;
    [self.eventDate resignFirstResponder];
}
- (void)LabelChange:(id)sender{
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    df.dateStyle = NSDateFormatterMediumStyle;
    self.eventDate.text = [NSString stringWithFormat:@"%@",
                      [df stringFromDate:self.datePicker.date]];
}

// Called when the UIKeyboardDidShowNotification is sent.
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height, 0.0);
    self.sv.contentInset = contentInsets;
    self.sv.scrollIndicatorInsets = contentInsets;
    
    // If active text field is hidden by keyboard, scroll it so it's visible
    // Your application might not need or want this behavior.
    CGRect aRect = self.view.frame;
    aRect.size.height -= kbSize.height;
    if (!CGRectContainsPoint(aRect, activeField.frame.origin) ) {
        CGPoint scrollPoint = CGPointMake(0.0, activeField.frame.origin.y-kbSize.height);
        [self.sv setContentOffset:scrollPoint animated:YES];
    }

}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    activeField = textField;

    //  RightBarButton.title  = @"Done";
    //itsRightBarButton.style = UIBarButtonItemStyleDone;
    //itsRightBarButton.target = self;
    
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    activeField = nil;
}

- (void)keyboardWillBeHidden:(NSNotification*)aNotification
    {
        UIEdgeInsets contentInsets = UIEdgeInsetsZero;
        self.sv.contentInset = contentInsets;
        self.sv.scrollIndicatorInsets = contentInsets;
    }

- (void)registerForKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasShown:)
                                                 name:UIKeyboardDidShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillBeHidden:)
                                                 name:UIKeyboardWillHideNotification object:nil];
    
}

- (IBAction)saveContact:(id)sender {
    self.contactData = [ContactData alloc];
    self.contactData.firstName = self.firstName.text;
    [[self contactData] setLastName:  self.lastName.text];
    [[self contactData] setInitialPoints: self.initialPoints.text];
    self.contactData.notes = self.notes.text;
    self.contactData.phoneNo = self.phoneNo.text;
    self.contactData.emailAddr = self.emailAddr.text;
    self.contactData.eventDate = self.eventDate.text;
    
    if ([ [self delegate] respondsToSelector:@selector(didAddContact:)]) {
        [ [self delegate] didAddContact:self.contactData];
    }
    [self dismissViewControllerAnimated:YES completion:nil];
}


// Contact - Picker Start

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
// Contact Picker code - end 


@end


