//
//  InputEventViewController.h
//  Test
//
//  Created by Venkatesh Arjunan on 10/7/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "InputViewController.h"
#import "Person.h"


@interface ContactData : NSObject {}
@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic, retain) NSString * points;
@property (nonatomic, retain) NSString * notes;
@property (nonatomic, retain) NSString * phoneNo;
@property (nonatomic, retain) NSString * emailAddr;
@property (nonatomic, retain) NSDate* date;
@property (nonatomic, retain) NSString *reminderFreq;
@property (nonatomic, retain) Person* selectedPerson;
@end


@interface InputEventViewController : UIViewController
//@property (nonatomic, assign) id<ContactAddDelegate> delegate;
@property (strong, nonatomic) Person* selectedPerson;
@property (strong, nonatomic) ContactData* contactData;
@end
