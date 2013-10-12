//
//  InputViewController.m
//  Test
//
//  Created by Venkatesh Arjunan on 9/26/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "InputViewController.h"
#import "SelectPersonViewController.h"
#import "Person.h"


@implementation ContactData

@end


@interface InputViewController ()
@property (weak, nonatomic) IBOutlet UITextField *firstName;
@property (weak, nonatomic) IBOutlet UITextField *lastName;
@property (weak, nonatomic) IBOutlet UITextField *emailAddr;
@property (weak, nonatomic) IBOutlet UIButton *selectContact;
@property (weak, nonatomic) IBOutlet UITextField *phoneNo;
@property (strong, nonatomic) ContactData* contactData;
@property (nonatomic,retain) UIBarButtonItem *nextButton;
@property (weak, nonatomic) Person* selectedPerson;
- (IBAction)showPhoneContacts:(id)sender;
- (IBAction)nextPage:(id)sender;
- (IBAction)selectExistingPerson:(id)sender;

@end

@implementation InputViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Initialization code here.
        
    }
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    self.nextButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(nextEvent)];
    self.nextButton.enabled = YES;
    self.navigationItem.rightBarButtonItem = self.nextButton;
    
}

- (void)nextEvent {
    
    [self performSegueWithIdentifier: @"InputEvent" sender: self];
    
}


- (IBAction)cancel:(id)sender {
}

- (IBAction)save:(id)sender {
    self.contactData = [ContactData alloc];
    self.contactData.firstName = self.firstName.text;
    self.contactData.lastName = self.lastName.text;
    self.contactData.phoneNo = self.phoneNo.text;
    self.contactData.emailAddr = self.emailAddr.text;
    
    // convert string to date
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    [df setDateFormat:@"yyyy-MM-dd hh:mm:ss a"];
//    self.contactData.date = [df dateFromString: self.date.text];
      
    if ([ [self delegate] respondsToSelector:@selector(didAddContact:)]) {
        [ [self delegate] didAddContact:self.contactData];
    }
    [self.navigationController popViewControllerAnimated:YES];
}

// this is needed to dismiss the keyboard which comes while typing

- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    
    if (theTextField == self.firstName) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.lastName) {
        [theTextField resignFirstResponder];
    }
  /*  else if (theTextField == self.points) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.notes) {
        [theTextField resignFirstResponder];
    }
   */
    else if (theTextField == self.emailAddr) {
        [theTextField resignFirstResponder];
    }
   /* else if (theTextField == self.date) {
        [theTextField resignFirstResponder];
    }*/
    else if (theTextField == self.phoneNo) {
        [theTextField resignFirstResponder];
    }
    return YES;
}


// Contact - Picker Start

- (IBAction)showPhoneContacts:(id)sender {
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


- (IBAction)nextPage:(id)sender {
     [self performSegueWithIdentifier: @"InputEvent" sender: self];
}

- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ( [[segue identifier] isEqualToString:@"SelectPerson"] )
    {
        SelectPersonViewController * destController = [segue destinationViewController];
        [destController setTitle:@"Select person "];
        [destController setExistingPersons:self.existingPersons];
    }
}


- (IBAction)selectExistingPerson:(id)sender {
    [self performSegueWithIdentifier: @"SelectPerson" sender: self];
}
@end
