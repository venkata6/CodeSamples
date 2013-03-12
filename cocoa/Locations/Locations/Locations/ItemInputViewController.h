//
//  ItemInputViewController.h
//  Locations
//
//  Created by Venkatesh Arjunan on 2/19/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ContactAddDelegate;

@interface ContactData : NSObject {
	
@private
	NSString *firstName;
	NSString *lastName;
	NSString *initialPoints;
    NSString *notes;
}

@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic, retain) NSString * initialPoints;
@property (nonatomic, retain) NSString * notes;

@end

@interface ItemInputViewController : UIViewController {
    ContactData *contactData;
 }

@property (nonatomic,retain) ContactData *contactData;

@property (nonatomic, assign) id<ContactAddDelegate> delegate;

@end

@protocol ContactAddDelegate <NSObject>
- (void) didAddContact:(ContactData *)contactData;
@end



