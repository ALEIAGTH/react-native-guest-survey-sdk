#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(GuestSurveySdk, NSObject)


RCT_EXTERN_METHOD(configure: (NSString)secretKey programKey:(NSString) programKey language:(NSString)language enableDebugging:(BOOL)enableDebugging callback:(RCTResponseSenderBlock)callback)
RCT_EXTERN_METHOD(startCollection: (NSString)user_Id)
RCT_EXTERN_METHOD(stopCollection)
RCT_EXTERN_METHOD(triggerSurvey: (NSString)programKey withEvent:(NSString)eventName parameters:(NSDictionary)parameters)
RCT_EXTERN_METHOD(getUserSurveys: (RCTResponseSenderBlock)callback)
RCT_EXTERN_METHOD(addPushNotificationToken:(NSString)token)
RCT_EXTERN_METHOD(sendLog)

@end
