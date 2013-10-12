//
//  InputViewController.h
//  Test
//
//  Created by Venkatesh Arjunan on 9/26/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AddressBookUI/AddressBookUI.h>

@protocol ContactAddDelegate;

@interface ContactData : NSObject {}

@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic, retain) NSString * points;
@property (nonatomic, retain) NSString * notes;
@property (nonatomic, retain) NSString * phoneNo;
@property (nonatomic, retain) NSString * emailAddr;
@property (nonatomic, retain) NSDate* date;

@end


@interface InputViewController : UIViewController <ABPeoplePickerNavigationControllerDelegate>

@property (nonatomic, assign) id<ContactAddDelegate> delegate;
@property (strong, nonatomic) NSArray* existingPersons;

@end

@protocol ContactAddDelegate <NSObject>
- (void) didAddContact:(ContactData *)contactData;
@end