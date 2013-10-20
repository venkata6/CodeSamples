//
//  Loc.h
//  Locations
//
//  Created by Venkatesh Arjunan on 2/17/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Loc : NSManagedObject

@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic,retain ) NSNumber * points;
@property (nonatomic, retain) NSDate * creationDate;
@property (nonatomic, retain) NSString * notes;
@property (nonatomic, retain) NSString * emailAddr;
@property (nonatomic, retain) NSString * phoneNo;

@end
