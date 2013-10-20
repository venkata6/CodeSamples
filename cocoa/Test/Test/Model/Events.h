//
//  Events.h
//  Test
//
//  Created by Venkatesh Arjunan on 10/20/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Person;

@interface Events : NSManagedObject

@property (nonatomic, retain) NSDate * date;
@property (nonatomic, retain) NSNumber * id;
@property (nonatomic, retain) NSString * notes;
@property (nonatomic, retain) NSNumber * points;
@property (nonatomic, retain) NSString * recurrence;
@property (nonatomic, retain) Person *doneBy;

@end
