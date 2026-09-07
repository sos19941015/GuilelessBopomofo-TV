# Guileless Bopomofo TV 樸實注音鍵盤 TV 版

這是 [Guileless Bopomofo 樸實注音鍵盤](https://github.com/hiroshiyui/GuilelessBopomofo) 的社群 Android TV 衍生版本，針對電視遙控器 DPAD 操作與客廳距離的顯示方式進行調整。

This is a community Android TV derivative of [Guileless Bopomofo](https://github.com/hiroshiyui/GuilelessBopomofo), adapted for TV remote DPAD navigation and living-room viewing distances.

## TV 版功能

- 支援 Android TV 遙控器方向鍵、確認鍵操作。
- 高對比黃色焦點框與放大效果。
- 鍵盤、候選字列及組字列縮為螢幕寬度 45%，並水平置中。
- 鍵盤周圍使用透明背景，減少遮擋播放內容。
- Android TV 模式與輸入法切換按鍵預設開啟。
- 提供 Android TV Launcher 入口及 16:9 TV banner。
- Debug 建置不包含 LeakCanary 的額外 Leaks 圖示。

## 下載與安裝

您可以從 [GitHub Releases](https://github.com/sos19941015/GuilelessBopomofo-TV/releases) 下載 APK，或在 Android TV 上使用 [Downloader app](https://www.aftvnews.com/downloader)：

1. 開啟 Downloader app，輸入代碼 **8284875**。
2. 也可以在 Downloader 或任何瀏覽器輸入 **`aftv.news/8284875`**。
3. 網頁開啟後下載 APK，依 Android TV 畫面指示允許該 App 安裝未知來源應用程式，然後完成安裝。

To download on Android TV, enter code **8284875** in the [Downloader app](https://www.aftvnews.com/downloader). You can also enter **`aftv.news/8284875`** in Downloader or any browser to open the destination URL.

## 原作者與致謝

本專案建立在 **YOU, Hui-Hong（[@hiroshiyui](https://github.com/hiroshiyui)）** 開發的 Guileless Bopomofo 之上。衷心感謝原作者長期投入開發、維護並以自由軟體方式公開原始碼，也感謝 [libchewing](https://github.com/chewing/libchewing) 的所有貢獻者。

原作者已在 [issue #74](https://github.com/hiroshiyui/GuilelessBopomofo/issues/74#issuecomment-5562545211) 回覆，同意依 `LICENSE` 與 `NOTICES.md` 的規範釋出程式碼，並說明原圖示可依 GPL-3.0-or-later 修改與散布。本 TV 版 banner 以原圖示為基礎製作並加入「TV」標示。

原專案的著作權聲明、[GPL-3.0 授權條款](./LICENSE)及[第三方元件聲明](./NOTICES.md)均完整保留。本衍生版本同樣依 GPL-3.0-or-later 提供。

---

## 原專案 README

>**聲明：** 本人已經離開軟體業，此專案僅能提供「維持與新版新酷音函式庫、新版 Android 相容性」此最小限度的維護更新，恕不再接受任何功能請求。
>
>**Statement:** I have left the software industry. This project will only provide minimal maintenance to ensure compatibility with new versions of the libchewing library and Android. No feature requests will be accepted.

# Guileless Bopomofo 樸實注音鍵盤

![Screenshot of Guileless Bopomofo](./media/Screenshot_20251223_234723.png)
![Screenshot of Guileless Bopomofo](./media/Screenshot_20251223_235026.png)

## About 這是啥

A [Bopomofo](https://en.wikipedia.org/wiki/Bopomofo) software keyboard (aka input method editor) on Android, which is utilizing [libchewing](http://chewing.im/) for intelligent phonetic processing.

樸實注音鍵盤是 Android 平台上的[注音符號](https://zh.wikipedia.org/wiki/%E6%B3%A8%E9%9F%B3%E7%AC%A6%E8%99%9F)軟體鍵盤（輸入法編輯器），藉由[新酷音輸入法](http://chewing.im/)程式庫的輔助，提供自動選字處理機能。

<a href='https://f-droid.org/zh_Hant/packages/org.ghostsinthelab.apps.guilelessbopomofo/'><img alt="F-Droid立即下載" src="./media/badge_get-it-on-zh-tw.png"/></a>

Please only download and install this software from the F-Droid App repository mentioned above, or through [the APK packages released by this project](https://github.com/hiroshiyui/GuilelessBopomofo/releases), to ensure your information security.

請只在以上所示的 F-Droid App 發行庫，或透過[本專案釋出之 APK 安裝套件包](https://github.com/hiroshiyui/GuilelessBopomofo/releases)取得、安裝本軟體，以保障您的資訊安全。

<strong>NOTICE:</strong> From 2025.09.08, this App is _not_ available on Google Play anymore.

<strong>敬請注意：</strong>自 2025.09.08 開始，本軟體<strong>不再</strong>於 Google Play 上架。

## Usage 使用

### Bopomofo keyboard layouts 注音鍵盤排列

In case you don't know: "Dachen" is the most common keyboard layout, which arranges Bopomofo symbols sequentially in columns.

As for "Hsu's Bopomofo Layout" and "E-Ten 26-Keys", they are more ergonomic Bopomofo keyboard layouts. One of the reasons I developed this input method editor is that I want to use a consistent Hsu's Bopomofo Layout on both my computer and my phone.

以防您不知道：「大千」就是市面上最常見的那種一列一列將注音符號依序排列的鍵盤。

至於「許氏鍵盤」與「倚天26鍵」、「大千26鍵」都是比較符合人體工學的三排式注音鍵盤排列，我之所以開發這個輸入法編輯器，原因之一，就是我想在電腦上、手機上使用一致的許氏鍵盤。

### Physical keyboard 實體鍵盤

Several common hotkeys:
  * <kbd>Left Shift</kbd> + <kbd>Space</kbd>: Switch between Bopomofo and Alphanumeric input modes.
  * <kbd>Alt</kbd> + <kbd>Space</kbd>: Switch between full-width and half-width character input modes.
  * <kbd>Alt</kbd> + <kbd>I</kbd>: Switch to other input methods.
  * Long press <kbd>Right Shift</kbd>: Open the common punctuation candidate list. Use <kbd>←</kbd><kbd>→</kbd> arrow keys to switch pages.
  * The <kbd>`</kbd> key (top-left of the keyboard): Open the symbol candidate list for various categories. Use <kbd>←</kbd><kbd>→</kbd> arrow keys to switch pages.

幾個常用控制鍵：
  * <kbd>左 Shift</kbd> + <kbd>空白鍵</kbd>：切換注音與英數字輸入模式
  * <kbd>Alt</kbd> + <kbd>空白鍵</kbd>：切換全形與半形
  * <kbd>Alt</kbd> + <kbd>I</kbd>：切換其他輸入法
  * 長按 <kbd>右 Shift</kbd>：開啟常用標點符號候選清單，按 <kbd>←</kbd><kbd>→</kbd> 鍵換頁
  * 鍵盤左上方的 <kbd>`</kbd> 鍵：開啟各種類別的符號候選清單，按 <kbd>←</kbd><kbd>→</kbd> 鍵換頁

## Build 組建

1. Get source code:
    ```bash
    git clone --recursive https://github.com/hiroshiyui/GuilelessBopomofo.git
    ```
1. Build it:
    * Import this project into Android Studio, then run **"Build -> Make Project"**, or...
    * execute `./gradlew :app:assembleDebug` or `./gradlew :app:assembleRelease` from shell command line
1. Locate the generated APK files from `./app/build/outputs/apk/`
1. Enjoy!

## Acknowledgements 感謝有您

* [Chewing contributors](http://chewing.im/about.html) 沒有這些高手維護新酷音，就沒有這個衍生的產品
* [Bobby Tung](https://bobtung.medium.com/) 推薦我精緻的注音符號字型
* [Wen-Chun Lin](https://github.com/cataska) 在我失意落魄時（雖說至今依然），多次從台北開車來宜蘭找我吃飯聊天
* [Jim Huang (jserv)](https://github.com/jserv) 從[很久很久以前](https://ghostsinthelab.org/2013/05/03/%e7%ad%86%e8%a8%98%ef%bc%9a%e7%b7%a8%e5%87%ba%e7%b5%a6-arm-linux-androideabi-%e7%94%a8%e7%9a%84-libchewing/)就鼓勵我把這個專案做出來，後來還贊助了我一大筆款項，讓我在遭逢筆電故障、有如窮途末路時，有錢可以組一台桌機繼續戰鬥
* [StarForcefield](https://starforcefield.wordpress.com/) 他寫的[新酷音範例程式](https://starforcefield.wordpress.com/2012/08/13/%e6%8e%a2%e7%b4%a2%e6%96%b0%e9%85%b7%e9%9f%b3%e8%bc%b8%e5%85%a5%e6%b3%95%ef%bc%9a%e4%bd%bf%e7%94%a8libchewing/)文章，啟發了我驗證跨平台編譯、執行 PoC 程式的可行性與效用
* [Weizhong Yang (zonble)](https://github.com/zonble) 如果沒有他推坑我實作支援實體鍵盤，我其實本來不想要做，您各位能爽爽用（或不爽卻湊合著、忍著用）實體鍵盤，請先謝謝他
* My dear friends, my family, and my wife.
