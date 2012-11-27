//
//  oAuth2TestViewController.h
//  oAuth2Test
//
//  Created by dominic dimarco on 5/22/10.
//  Copyright 214Apps 2010. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface oAuth2TestViewController : UIViewController <UIWebViewDelegate> {

	IBOutlet UIWebView *webView;
	
}
@property (nonatomic, retain) UIWebView *webView;

@end

