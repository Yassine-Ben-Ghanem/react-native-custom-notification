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

export function CreateInformativeNotification(title: String, description: String, imageUrl:String){
  return CustomNotification.CreateInformativeNotification(title,description,imageUrl);
}

export function CreateEvaluativeNotification(){
  return CustomNotification.CreateEvaluativeNotification();
}

export function CreateBigPictureNotification(title: String, description: String, avatarUrl:String, bigPicture:String){
  return CustomNotification.CreateBigPictureNotification(title,description,avatarUrl,bigPicture)
}

export function CreatePromotionNotification(title: String, description: String, avatarUrl:String, bigPicture:String){
  return CustomNotification.CreatePromotionNotification(title,description,avatarUrl,bigPicture)
}