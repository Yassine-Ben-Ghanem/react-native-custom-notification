import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-custom-notification' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const CustomNotification = NativeModules.CustomNotification
  ? NativeModules.CustomNotification
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function CreateInformativeNotification(title: String, description: String, imageUrl:String,actionNotification:String){
  return CustomNotification.CreateInformativeNotification(title,description,imageUrl,actionNotification);
}

export function CreateAnimationNotification(){
  return CustomNotification.CreateAnimationNotification();
}

export function CreateBigPictureNotification(title: String, description: String, avatarUrl:String, bigPictureUrl:String,actionNotification:String){
  return CustomNotification.CreateBigPictureNotification(title,description,avatarUrl,bigPictureUrl,actionNotification,)
}

export function CreateProductNotification(title: String, description: String, avatarUrl:String, bigPicture:String,productTitle:String,productDescription:String,actionNotification:String){
  return CustomNotification.CreateProductNotification(title,description,avatarUrl,bigPicture,productTitle,productDescription,actionNotification)
}

// export function CreateActionButtonNotification(){
//   return CustomNotification.button()
// }

export function CreateNotificationUrl(title: String, description: String, imageUrl:String,url:string,message:String){
  return CustomNotification.CreateNotificationUrl(title,description,imageUrl,url,message)
}