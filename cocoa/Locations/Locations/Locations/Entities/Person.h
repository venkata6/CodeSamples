//
//  Person.h
//  Locations
//
//  Created by Venkatesh Arjunan on 9/14/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Events;

@interface Person : NSManagedObject

@property (nonatomic, retain) NSString * emailAddr;
@property (nonatomic, retain) NSString * firstName;
@property (nonatomic, retain) NSNumber * id;
@property (nonatomic, retain) NSString * lastName;
@property (nonatomic, retain) NSString * notes;
@property (nonatomic, retain) NSString * phoneNumber;
@property (nonatomic, retain) NSSet *hasDeeds;
@end

@interface Person (CoreDataGeneratedAccessors)

- (void)addHasDeedsObject:(Events *)value;
- (void)removeHasDeedsObject:(Events *)value;
- (void)addHasDeeds:(NSSet *)values;
- (void)removeHasDeeds:(NSSet *)values;

@end
