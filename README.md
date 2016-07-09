# LuckDraw


The effect：

<img src="http://fanrunqi.github.io/images/LuckDraw/1.gif" width = "232" height = "386"  />

# Usage

## Step 1

> Gradle
```
compile 'cn.fanrunqi:luckdrawlibrary:1.0.1'
```
> Maven
```
<dependency>
  <groupId>cn.fanrunqi</groupId>
  <artifactId>luckdrawlibrary</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

## Step 2

> the code to set text and pic.

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


> the layout with your picture width and height.

```
<cn.fanrunqi.luckdrawlibrary.LuckDraw
        android:id="@+id/luckdraw"
        android:layout_width="379dp"
        android:layout_height="126dp" />
```
