require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RN-polar-ble-sdk"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  RN-polar-ble-sdk
                   DESC
  s.homepage     = "https://github.com/MAKman1/RN-Polar-BLE-SDK"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "Joseph Larralde" => "joseph.larralde@gmail.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/MAKman1/RN-Polar-BLE-SDK.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  # ...
  # s.dependency "..."

  # see https://github.com/brodybits/create-react-native-module/issues/207#issuecomment-713638978
  s.vendored_frameworks = "polar-ble-sdk/polar-sdk-ios/RxSwift.framework", "polar-ble-sdk/polar-sdk-ios/PolarBleSdk.framework"
end

