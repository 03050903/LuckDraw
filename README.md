# LuckDraw


The effect：

<img src="https://raw.githubusercontent.com/fanrunqi/LuckDraw/master/screenshots/1.gif" width = "232" height = "386"  />

# Usage

## dependency

> Gradle
```
compile 'cn.fanrunqi:luckdrawlibrary:1.0.2'
```
> Maven
```
<dependency>
  <groupId>cn.fanrunqi</groupId>
  <artifactId>luckdrawlibrary</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```

## layout

> the layout with your picture width and height.

```
<cn.fanrunqi.luckdrawlibrary.LuckDraw
        android:id="@+id/luckdraw"
        android:layout_width="379dp"
        android:layout_height="126dp" />
```
## code

> to set text and pic.

```
  luckdraw.Init(String text,int drawableResId); //"$7000000",R.drawable.luckdraw_fg
  luckdraw.setText(String TextColor,int TextSize);//"#dddddd",60
  luckdraw.setStrokeWidth(int strokeWidth);//50
  luckdraw.setOnCompleteListener(new LuckDraw.CompleteListener() {
            @Override
            public void complete() {
               //do something
            }
        });
```

# License
> Copyright 2016 fanrunqi

> Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  >  http://www.apache.org/licenses/LICENSE-2.0

> Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
