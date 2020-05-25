# MiMiYaHelper
DDouyin sharp tool,floating window,copy sharp url to get raw video



In Android 10 cannot listen to the clipboard,so we need a EditText on top Windows,hook global focus. 

Floating window button,click it to download raw video to self cloud.


##
<div>
  <a><img src="./snapshoot/WX20200517-173855@2x.png" height="30%" width="30%"></a>           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</div>

## 服务端加解密调用接口文档  Gorgon Xlog TTEncrypt
##### 后台接口Java开发,不对外,有需要私聊

```python

import requests
import time

# 注册新设备
new_device = requests.post("http://127.0.0.1:5016/device_register/", json={}).json()
print(new_device)
#{'status': 'ok', 'data': {'device_id': '***', 'uuid': '***', 'openudid': '***', 'cdid': '***', 'mc': '***', 'sim_serial_number': '***', 'clientudid': '***', 'req_id': '***', 'build_serial': '***', 'first_reg_time': ***, 'install_id': '***', 'new_user': ***, 'channel': '***', 'os_api': '***', 'device_type': '***'}, 'agent': '***'}


device_id = new_device["data"]["device_id"]
print(device_id)

# 刷新设备注册信息
refresh_device = requests.post("http://127.0.0.1:5016/device_register/", json={"device_id": device_id}).json()
print(refresh_device)

# 获取设备注册参数
device_params = requests.get("http://127.0.0.1:5016/device_params/" + device_id).json()
print(device_params)

# 请求示例
millis = int(time.time() * 1000)
millis_short = int(millis / 1000)

sec_user_id = '***************************************'

params = dict([m.split("=") for m in device_params["data"].split("&")])

url = f"https://aweme.snssdk.com/aweme/v1/aweme/favorite/?invalid_item_count=0&is_hiding_invalid_item=0&max_cursor=0&" \
      f"sec_user_id={sec_user_id}&count=20&os_api={params['os_api']}&device_type={params['device_type']}&" \
      f"ssmix=a&manifest_version_code=100601&dpi=480&uuid={params['uuid']}&app_name=aweme&version_name=10.6.0&" \
      f"ts={str(millis_short)}&app_type=normal&ac=wifi&host_abi=armeabi-v7a&update_version_code=10609900&channel=xiaomi&" \
      f"_rticket={str(millis)}&device_platform=android&iid={params['iid']}&version_code=100600&" \
      f"cdid={params['cdid']}&openudid={params['openudid']}&device_id={params['device_id']}&" \
      f"resolution=1080*1920&os_version=6.0.1&language=zh&device_brand=Xiaomi&aid=1128"

data = {"url": url, "millis": millis_short}
gorgonInfo = requests.post("http://127.0.0.1:5016/gorgon", json=data).json()

print(url, "\n", gorgonInfo)
#{'khronos': 1590373639, 'gorgon': '040100800800ccfdf0b233263c9252381b50880d73efe2b7af06', 'status': 'ok'}

headers = {
	"X-SS-REQ-TICKET": str(millis),
	"sdk-version": "1",
	"Host": "aweme.snssdk.com",
	"Connection": "Keep-Alive",
	"User-Agent": gorgonInfo["agent"],
	"X-Gorgon": gorgonInfo["gorgon"],
	"X-Khronos": str(gorgonInfo["khronos"])
}
response = requests.get(url, headers=headers)

print(response.text)

# xlog 加密
endata = requests.post("http://127.0.0.1:5016/xlog/en", data="加密字符串密密密密密密密密密密密密密密密密密".encode("utf-8")).json()
print(endata)
#{'message': '020ef5297ff661fc03a01abbada969a0a65eb130d5e858c06f03fd2712a2f8a51af34581d895f924455793e99ca7c276aac6afd7ef6c404ecaa76691c2a2bcd7d0f37a7370e28599f87f81468e459c9947b0f5ffbf', 'status': 'ok'}

# xlog 解密
dedata = requests.post("http://127.0.0.1:5016/xlog/de", data=endata["message"].encode("utf-8")).json()
print(dedata)
#{'message': '加密字符串密密密密密密密密密密密密密密密密密', 'status': 'ok'}

```

### 服务端功能调用接口文档 Login SmsCode UserInfo ExtractShareUrl DownloadVideo SaveVideo PushVideo 
```
//todo


```






## 协议

本项目基于GNU开源协议，不得用于商业用途

本项目仅供学习交流使用，本人不承担任何法律责任

若有侵权请联系删除。

## 支持

  * **您的赞赏是让我加快开发进度的最大鼓励.**

<div>
  <a><img src="https://github.com/satng/images/blob/master/WX20200520-084311%402x.png" height="30%" width="30%"></a>           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <a><img src="https://github.com/satng/images/blob/master/WX20200520-084414%402x.png" height="32%" width="32%"></a>
</div>
