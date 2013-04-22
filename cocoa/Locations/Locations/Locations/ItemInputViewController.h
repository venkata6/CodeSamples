//
//  ItemInputViewController.h
//  Locations
//
//  Created by Venkatesh Arjunan on 2/19/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AddressBookUI/AddressBookUI.h>

@protocol ContactAddDelegate;

@interface ContactData : NSObject {
	
@private
	NSString *firstName;
	NSString *lastName;
	NSString *initialPoints;
    NSString *notes;
    NSString *phoneNo;
    NSString *emailAddr;
    NSDate *eventDate;
}

@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic, retain) NSString * initialPoints;
@property (nonatomic, retain) NSString * notes;
@property (nonatomic, retain) NSString * phoneNo;
@property (nonatomic, retain) NSString * emailAddr;
@property (nonatomic, retain) NSDate* eventDate;

@end

@interface ItemInputViewController : UIViewController <ABPeoplePickerNavigationControllerDelegate,
    UITextFieldDelegate>{
    ContactData *contactData;
    float keyBoardOffset;

 }
- (IBAction)saveContact:(id)sender;
@property (strong, nonatomic) IBOutlet UIScrollView *sv;
@property (weak, nonatomic) IBOutlet UIDatePicker *datePicker;
@property (weak, nonatomic) IBOutlet UIButton *selectButton;
@property (weak, nonatomic) IBOutlet UITextField *phoneNo;
@property (weak, nonatomic) IBOutlet UITextField *firstName;
@property (weak, nonatomic) IBOutlet UITextField *lastName;
@property (weak, nonatomic) IBOutlet UITextField *notes;
@property (weak, nonatomic) IBOutlet UITextField *initialPoints;
@property (weak, nonatomic) IBOutlet UITextField *emailAddr;
@property (weak, nonatomic) IBOutlet UITextField *eventDate;
- (IBAction)showPicker:(id)sender;
- (IBAction)cancel:(id)sender;
- (IBAction)selectDate:(id)sender;

@property (nonatomic,retain) ContactData *contactData;
@property (nonatomic,assign) float keyBoardOffset;
//@property (retain,nonatomic) IBOutlet UIScrollView* sv;
@property (nonatomic, assign) id<ContactAddDelegate> delegate;


@end

@protocol ContactAddDelegate <NSObject>
- (void) didAddContact:(ContactData *)contactData;
@end



