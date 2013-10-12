//
//  SelectPersonViewController.h
//  Test
//
//  Created by Venkatesh Arjunan on 10/12/13.
//  Copyright (c) 2013 Venkatesh Arjunan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SelectPersonViewController : UIViewController<UIPickerViewDelegate,UIPickerViewDataSource>
@property (strong, nonatomic) NSArray* existingPersons;
@end
