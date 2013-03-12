//
//  ItemInputViewController.m
//  Locations
//
//  Created by Venkatesh Arjunan on 2/19/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import "ItemInputViewController.h"

@interface ItemInputViewController ()
@property (weak, nonatomic) IBOutlet UITextField *firstName;
@property (weak, nonatomic) IBOutlet UITextField *lastName;
- (IBAction)saveContact:(id)sender;
- (IBAction)cancel:(id)sender;
@property (weak, nonatomic) IBOutlet UITextField *notes;
@property (weak, nonatomic) IBOutlet UITextField *initialPoints;
@end

@implementation ContactData

@synthesize firstName,lastName,initialPoints,notes;

@end

@implementation ItemInputViewController

@synthesize contactData;
@synthesize delegate;


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
    return YES;
}

@end


