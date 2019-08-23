//
//  ViewController.m
//  SampleApp
//
//  Created by Patel, Varun on 11/26/18.
//  Copyright © 2018 First Data. All rights reserved.
//

#import "ViewController.h"
#import <WebKit/WebKit.h>

//SDK:
#import "PaymentSDK/PaymentSDK-Swift.h"
#import "PaymentSDK/PaymentSDK.h"

@interface ViewController (Private)
- (void) _startSDKWithConfigurationDictionary:(NSDictionary*)configurationDictionary;
@end

@interface ViewController () <WKUIDelegate,WKNavigationDelegate>{
    WKWebView *_webView;
}
@end

@implementation ViewController {
    UIActivityIndicatorView *_activityIndicatorView;
}

- (void) loadView {
    _webView = [[WKWebView alloc] initWithFrame:[UIScreen mainScreen].bounds];
    _webView.UIDelegate = self;
    _webView.navigationDelegate = self;
    [self setView:_webView];
}

- (void) viewDidLoad {
    [super viewDidLoad];
    self.title = @"CP SDK Demo - QA";
    self.navigationController.navigationBar.tintColor = [UIColor colorWithRed:81/255. green:142/255. blue:176/255. alpha:1];
    
    _activityIndicatorView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
    _activityIndicatorView.color = [UIColor colorWithRed:81/255. green:142/255. blue:176/255. alpha:1];
    _activityIndicatorView.center = self.view.center;
    [self.view addSubview:_activityIndicatorView];
    UIBarButtonItem *envButton = [[UIBarButtonItem alloc] initWithTitle:@"CAT" style:UIBarButtonItemStylePlain target:self action:@selector(environmentButtonTapped:)];
    self.navigationItem.rightBarButtonItem = envButton;
    [self loadSampleAppPage:EnvironmentQa];
}

- (void)loadSampleAppPage:(Environment)environment {
    [_activityIndicatorView startAnimating];
    NSString *requestURL;
    if (environment == EnvironmentQa) {
        requestURL = @"https://qa.api.firstdata.com/connectpayapi/v1/static/v3/tool/internal/cp-sdk.html";
    } else {
        requestURL = @"https://cat.api.firstdata.com/connectpayapi/v1/static/v3/tool/internal/cp-sdk.html";
    }
    requestURL = @"https://qa.api.firstdata.com/connectpayapi/v1/static/v3/tool/internal/cp-sdk.html?apiKey=5e66b5d4iYHVK9ChqaFMnCrgfBBbIwiW&secretKey=iGMK11IfsGD00vEW";
    requestURL = @"https://qa.api.firstdata.com/connectpayapi/v1/static/v3/tool/internal/cp-sdk.html?apiKey=P8uAm7Af2xIOZQvfZCsQy3IJ3oqKpm6l&secretKey=QTzAYKHFufFND32H";

    NSURLRequest *hostedPageRequest = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:requestURL]];
    
    // Load
    dispatch_async(dispatch_get_main_queue(), ^{
        [self->_webView loadRequest:hostedPageRequest];
    });
}

- (void)environmentButtonTapped:(UIBarButtonItem *)barButton {
    if ([barButton.title isEqualToString:@"QA"]) {
        barButton.title = @"CAT";
        self.title = @"CP SDK Demo - QA";
        [self loadSampleAppPage:EnvironmentQa];
    } else {
        barButton.title = @"QA";
        self.title = @"CP SDK Demo - CAT";
        [self loadSampleAppPage:EnvironmentCat];
    }
}

- (void) failedMASRequestWithErrorCode:(NSString *)errorCode andDescription:(NSString *)description {
    UIAlertController *errorAlert = [UIAlertController alertControllerWithTitle:errorCode message:description preferredStyle:UIAlertControllerStyleAlert];
    __weak typeof(self) weakSelf = self;
    UIAlertAction *okButton = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
        __strong typeof(weakSelf) strongSelf = weakSelf;
        [strongSelf.navigationController popViewControllerAnimated:YES];
    }];
    [errorAlert addAction:okButton];
    [self presentViewController:errorAlert animated:YES completion:nil];
}

#pragma mark WKNavigationDelegate methods
- (void) webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler {
    
    //Step 4 - Handle finish redirection
    NSString *requestURL = navigationAction.request.URL.absoluteString;
    
    NSLog(@"Redirect has occurred ...");
    NSLog(@"Request Url: %@", requestURL);
    
    if ([requestURL hasPrefix:@"sdk://start"]) {
        // This is the clue that Hosted Posted is finished
        NSLog(@" --------------------------------------------------------------");
        NSLog(@"| REQUEST URL: %@", requestURL);
        NSLog(@" --------------------------------------------------------------");
        
        NSString *sdkConfigurationString = [requestURL componentsSeparatedByString:@"sdk://start?config="].lastObject;
        sdkConfigurationString = [sdkConfigurationString stringByRemovingPercentEncoding];
        NSData *sdkConfigurationData = [sdkConfigurationString dataUsingEncoding:NSUTF8StringEncoding];
        NSDictionary *sdkConfigurationDictionary = [NSJSONSerialization JSONObjectWithData:sdkConfigurationData options:kNilOptions error:nil];
        [self _startSDKWithConfigurationDictionary:sdkConfigurationDictionary];
        
        if (![requestURL hasPrefix:@"ucom://finish?result=failure"]) {
            [self.navigationController popViewControllerAnimated:YES];
        }
        decisionHandler(WKNavigationActionPolicyCancel);
    } else {
        // Otherwise don't intercept
        decisionHandler(WKNavigationActionPolicyAllow);
    }
}


- (void) webView:(WKWebView *)webView didFailNavigation:(null_unspecified WKNavigation *)navigation withError:(NSError *)error{
    NSLog(@"didFailNavigation ...");
}

- (void) webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation {
    NSString *execTemplate = @"sdkClient.nativeInit();";
    [_webView evaluateJavaScript:execTemplate completionHandler:nil];
    
    if ([_activityIndicatorView isAnimating]) {
        [_activityIndicatorView stopAnimating];
    }
}

- (void) cancelButtonTapped {
    [self.navigationController popViewControllerAnimated:YES];
}
@end

@implementation ViewController (Private)
- (void) _startSDKWithConfigurationDictionary:(NSDictionary*)configurationDictionary {
    NSString *sdk = configurationDictionary[@"sdk"];
    NSDictionary *extraParameters = configurationDictionary[@"extraParams"];
    if ([sdk.lowercaseString isEqualToString:@"cp"]) {
        NSArray *rel = [configurationDictionary[@"rel"] componentsSeparatedByString:@"."];
        NSString *flow = rel[1];
        NSString *suffix = rel.lastObject;
        if ([suffix.lowercaseString isEqualToString:@"Web".lowercaseString]) {
            UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert" message:@"NON-MOBILE Flow, Please select a Mobile Flow." preferredStyle:UIAlertControllerStyleAlert];
            [alertController addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:nil]];
            [self presentViewController:alertController animated:YES completion:nil];
            return;
        }
        NSString *environment = [configurationDictionary[@"env"] stringByTrimmingCharactersInSet:NSCharacterSet.whitespaceAndNewlineCharacterSet].lowercaseString;
        Environment sdkEnv;
        if ([environment isEqualToString:@"qa"]) {
            sdkEnv = EnvironmentQa;
        } else if ([environment isEqualToString:@"cat"] || [environment isEqualToString:@"int"]) {
            sdkEnv = EnvironmentCat;
        } else {
            sdkEnv = EnvironmentProd;
        }
        CPSDK *cpSdk = [[CPSDK alloc] initWithApiKey:configurationDictionary[@"apiKey"] andEnvironment:sdkEnv];
        NSString *fdCustomerId = [configurationDictionary[@"fdCustomerId"] stringByTrimmingCharactersInSet:NSCharacterSet.whitespaceAndNewlineCharacterSet];
        NSString *encryptionKey = [configurationDictionary[@"encryptionKey"] stringByTrimmingCharactersInSet:NSCharacterSet.whitespaceAndNewlineCharacterSet];
        NSString *accessToken = [configurationDictionary[@"accessToken"] stringByTrimmingCharactersInSet:NSCharacterSet.whitespaceAndNewlineCharacterSet];
        NSString *configId = [configurationDictionary[@"configId"] stringByTrimmingCharactersInSet:NSCharacterSet.whitespaceAndNewlineCharacterSet];
        NSString *postUrl = [configurationDictionary[@"postUrl"] stringByTrimmingCharactersInSet:NSCharacterSet.whitespaceAndNewlineCharacterSet];
        CPSdkConfiguration *configuration = [[CPSdkConfiguration alloc] initWithFdCustomerId:fdCustomerId encryptionKey:encryptionKey accessToken:accessToken configId:configId andPostUrl:postUrl];
        if ([flow.lowercaseString isEqualToString:@"ManualDepositWithAccountField".lowercaseString] ||
            [flow.lowercaseString isEqualToString:@"ManualDeposit".lowercaseString]) {
            ManualDepositConfiguration *manualDepositConfiguration = [[ManualDepositConfiguration alloc] init];
            manualDepositConfiguration.accountNumber = extraParameters[@"accountNumber"];
            manualDepositConfiguration.firstDepositedAmount = extraParameters[@"firstDepositedAmount"];
            manualDepositConfiguration.secondDepositedAmount = extraParameters[@"secondDepositedAmount"];
            ManualDeposit *manualDeposit = [cpSdk manualDepositWithCpSdkConfiguration:configuration andManualDepositConfiguration:manualDepositConfiguration];
            [manualDeposit startWithCompletionHandler:^(NSDictionary<NSString *,id> *result) {
                NSLog(@"%@", result.description);
            }];
        } else if ([flow.lowercaseString isEqualToString:@"CloseAccount".lowercaseString]) {
            CloseAccountConfiguration *closeAccountConfiguration = [[CloseAccountConfiguration alloc] initWithAccountNumber:extraParameters[@"accountNumber"] andReason:@""];
            CloseAccount *closeAccount = [cpSdk closeAccountWithCpSdkConfiguration:configuration andCloseAccountConfiguration:closeAccountConfiguration];
            [closeAccount startWithCompletionHandler:^(NSDictionary<NSString *,id> *result) {
                NSLog(@"%@", result.description);
            }];
        } else if ([flow.lowercaseString isEqualToString:@"UpdateEnrollment".lowercaseString] || [flow.lowercaseString isEqualToString:@"EnrollmentOption".lowercaseString] || [flow.lowercaseString isEqualToString:@"ManualOnly".lowercaseString] || [flow.lowercaseString isEqualToString:@"EnrollmentBothOption".lowercaseString] || [flow.lowercaseString isEqualToString:@"BankOnly".lowercaseString] || [flow.lowercaseString isEqualToString:@"EnrollmentAccountDetails".lowercaseString]) {
            ManualEnrollmentConfiguration *manualEnrollmentConfiguration = [[ManualEnrollmentConfiguration alloc] init];
            for (NSString *key in extraParameters.allKeys) {
                if ([key isEqualToString:@"routingNumber"]) {
                    manualEnrollmentConfiguration.routingNumber = extraParameters[key];
                } else if ([key isEqualToString:@"accountNumber"]) {
                    manualEnrollmentConfiguration.accountNumber = extraParameters[key];
                } else if ([key isEqualToString:@"accountType"]) {
                    manualEnrollmentConfiguration.accountType = extraParameters[key];
                } else if ([key isEqualToString:@"onlineBankTransactionId"]) {
                    manualEnrollmentConfiguration.onlineBankTransactionId = extraParameters[key];
                } else if ([key isEqualToString:@"connectPayPaymentNumber"]) {
                    manualEnrollmentConfiguration.cpCardNumber = extraParameters[key];
                } else if ([key isEqualToString:@"firstName"]) {
                    manualEnrollmentConfiguration.firstName = extraParameters[key];
                } else if ([key isEqualToString:@"lastName"]) {
                    manualEnrollmentConfiguration.lastName = extraParameters[key];
                } else if ([key isEqualToString:@"email"]) {
                    manualEnrollmentConfiguration.email = extraParameters[key];
                } else if ([key isEqualToString:@"street"]) {
                    manualEnrollmentConfiguration.streetAddress = extraParameters[key];
                } else if ([key isEqualToString:@"street2"]) {
                    manualEnrollmentConfiguration.apartmentNumber = extraParameters[key];
                } else if ([key isEqualToString:@"city"]) {
                    manualEnrollmentConfiguration.city = extraParameters[key];
                } else if ([key isEqualToString:@"state"]) {
                    manualEnrollmentConfiguration.state = extraParameters[key];
                } else if ([key isEqualToString:@"postalCode"]) {
                    manualEnrollmentConfiguration.zipCode = extraParameters[key];
                } else if ([key isEqualToString:@"driversLicense"]) {
                    manualEnrollmentConfiguration.driversLicense = extraParameters[key];
                } else if ([key isEqualToString:@"driversLicenseIssuingState"]) {
                    manualEnrollmentConfiguration.driversLicenseIssuingState = extraParameters[key];
                } else if ([key isEqualToString:@"ssn"]) {
                    manualEnrollmentConfiguration.ssn = extraParameters[key];
                } else if ([key isEqualToString:@"gender"]) {
                    manualEnrollmentConfiguration.gender = extraParameters[key];
                } else if ([key isEqualToString:@"dob"]) {
                    manualEnrollmentConfiguration.dob = extraParameters[key];
                } else if ([key isEqualToString:@"pin"] || [key isEqualToString:@"oldPin"]) {
                    manualEnrollmentConfiguration.pin = extraParameters[key];
                } else if ([key isEqualToString:@"newPin"]) {
                    manualEnrollmentConfiguration.newPin = extraParameters[key];
                } else if ([key isEqualToString:@"phone"]) {
                    NSArray *phones = extraParameters[key];
                    NSMutableArray *phonesMutableArray = [NSMutableArray array];
                    for (NSDictionary *phone in phones) {
                        PhoneNumberConfiguration *phoneConfiguration = [[PhoneNumberConfiguration alloc] init];
                        phoneConfiguration.phoneNumber = phone[@"number"];
                        phoneConfiguration.type = phone[@"type"];
                        [phonesMutableArray addObject:phoneConfiguration];
                    }
                    manualEnrollmentConfiguration.phoneNumbers = phonesMutableArray.copy;
                } else if ([key isEqualToString:@"genericFlag1"]) {
                    manualEnrollmentConfiguration.genericFlag1 = extraParameters[key];
                } else if ([key isEqualToString:@"genericFlag2"]) {
                    manualEnrollmentConfiguration.genericFlag2 = extraParameters[key];
                } else if ([key isEqualToString:@"genericFlag3"]) {
                    manualEnrollmentConfiguration.genericFlag3 = extraParameters[key];
                } else if ([key isEqualToString:@"genericCode1"]) {
                    manualEnrollmentConfiguration.genericCode1 = extraParameters[key];
                } else if ([key isEqualToString:@"genericCode2"]) {
                    manualEnrollmentConfiguration.genericCode2 = extraParameters[key];
                } else if ([key isEqualToString:@"genericCode3"]) {
                    manualEnrollmentConfiguration.genericCode3 = extraParameters[key];
                } else if ([key isEqualToString:@"reportingField1"]) {
                    manualEnrollmentConfiguration.reportingField1 = extraParameters[key];
                } else if ([key isEqualToString:@"reportingField2"]) {
                    manualEnrollmentConfiguration.reportingField2 = extraParameters[key];
                } else if ([key isEqualToString:@"reportingField3"]) {
                    manualEnrollmentConfiguration.reportingField3 = extraParameters[key];
                }
            }
            if ([flow.lowercaseString isEqualToString:@"EnrollmentOption".lowercaseString] || [flow.lowercaseString isEqualToString:@"ManualOnly".lowercaseString] || [flow.lowercaseString isEqualToString:@"EnrollmentBothOption".lowercaseString] || [flow.lowercaseString isEqualToString:@"BankOnly".lowercaseString]) {
                ManualEnrollment *manualEnrollment = [cpSdk manualEnrollmentWithCpSdkConfiguration:configuration andManualEnrollmentConfiguration:manualEnrollmentConfiguration];
                [manualEnrollment startWithCompletionHandler:^(NSDictionary<NSString *,id> *result) {
                    NSLog(@"%@", result.description);
                }];
            } else if ([flow.lowercaseString isEqualToString:@"UpdateEnrollment".lowercaseString]) {
                UpdateEnrollment *updateEnrollment = [cpSdk updateEnrollmentWithCpSdkConfiguration:configuration andUpdateEnrollmentConfiguration:manualEnrollmentConfiguration];
                [updateEnrollment startWithCompletionHandler:^(NSDictionary<NSString *,id> *result) {
                    NSLog(@"%@", result.description);
                }];
            } else if ([flow.lowercaseString isEqualToString:@"EnrollmentAccountDetails".lowercaseString]) {
                EnrollmentAccountDetails *enrollmentAccountDetail = [cpSdk enrollmentAccountDetailWithCpSdkConfiguration:configuration andEnrollmentAccountDetailConfiguration:manualEnrollmentConfiguration];
                [enrollmentAccountDetail startWithCompletionHandler:^(NSDictionary<NSString *,id> *result) {
                    NSLog(@"%@", result.description);
                }];
            }
        } else if ([flow.lowercaseString isEqualToString:@"AccountValidations".lowercaseString] ||
                   [flow.lowercaseString isEqualToString:@"AccountValidation".lowercaseString]) {
            AccountValidationConfiguration *accountValidationConfiguration = [[AccountValidationConfiguration alloc] init];
            for (NSString *key in extraParameters.allKeys) {
                if ([key isEqualToString:@"pin"]) {
                    accountValidationConfiguration.pin = extraParameters[key];
                } else if ([key isEqualToString:@"connectPayPaymentNumber"]) {
                    accountValidationConfiguration.cpCardNumber = extraParameters[key];
                }
            }
            AccountValidation *accountValidation = [cpSdk accountValidationWithCpSdkConfiguration:configuration andAccountValidationConfiguration:accountValidationConfiguration];
            [accountValidation startWithCompletionHandler:^(NSDictionary<NSString *,id> *result) {
                NSLog(@"%@", result.description);
            }];
        } else {
            UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert" message:@"Un-implemented flow detected in sdk" preferredStyle:UIAlertControllerStyleAlert];
            [alertController addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:nil]];
            [self presentViewController:alertController animated:YES completion:nil];
        }
    }
}
@end
