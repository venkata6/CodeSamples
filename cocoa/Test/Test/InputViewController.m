//
//  InputViewController.m
//  Test
//
//  Created by Venkatesh Arjunan on 9/26/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "InputViewController.h"
#import "SelectPersonViewController.h"
#import "InputEventViewController.h"
#import "Person.h"


@interface InputViewController ()
@property (weak, nonatomic) IBOutlet UITextField *firstName;
@property (weak, nonatomic) IBOutlet UITextField *lastName;
@property (weak, nonatomic) IBOutlet UITextField *emailAddr;
@property (weak, nonatomic) IBOutlet UIButton *selectContact;
@property (weak, nonatomic) IBOutlet UITextField *phoneNo;
@property (retain, nonatomic) Person* selectedPerson;
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
    // AppDelegate * appDelegate = (AppDelegate *) [[UIApplication sharedApplication] delegate];
    
}

// this is needed to dismiss the keyboard which comes while typing

- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    
    if (theTextField == self.firstName) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.lastName) {
        [theTextField resignFirstResponder];
    }
    else if (theTextField == self.emailAddr) {
        [theTextField resignFirstResponder];
    }
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
   //     [destController setDelegate:self];
        [destController setExistingPersons:self.existingPersons];
    }
    else if ( [[segue identifier] isEqualToString:@"InputEvent"] )
    {
        InputEventViewController * destController = [segue destinationViewController];
        [destController setTitle:@"Enter Event Details"];
        if ( self.selectedPerson == nil ){
            destController.contactData = [ContactData alloc];
            destController.contactData.firstName = self.firstName.text;
            destController.contactData.lastName = self.lastName.text;
            destController.contactData.phoneNo = self.phoneNo.text;
            destController.contactData.emailAddr = self.emailAddr.text;
        }
        else {
            [destController setSelectedPerson:self.selectedPerson];
        }
    }
}


- (IBAction)selectExistingPerson:(id)sender {
    [self performSegueWithIdentifier: @"SelectPerson" sender: self];
}
@end
