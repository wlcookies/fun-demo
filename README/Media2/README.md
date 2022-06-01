
## MediaItem

> 包含有关具有元数据信息的单个媒体项目的信息的类，通过 **MediaItem.Builder** 创建

- MediaItem.Builder
	> 构建MediaItem对象
  
- POSITION_UNKNOWN
	> 当位置未知时使用

- getEndPosition()
	> 以毫秒为单位，返回播放结束的位置

- getMetadata()
  > **MediaMetadata**，获取媒体的元数据

- getStartPosition()
	> 以毫秒为单位，返回播放开始的位置

- setMetadata(MediaMetadata metadata)
	> 设置媒体元数据

## MediaMetadata

> 包含项目的有关元数据
> 
> 注意一下几点:
> - **Media ID** ：如果设置了ID，这个ID必须是持久键，便于MediaController、MediaBrowser存储信息以后访问它
> - **Browsable type**：定义了媒体项是否拥有子项以及子项的类型，这样MediaBrowser就知道后续的MediaBrowser.getChildren是否能成功运行
> - **Playable type**：定义媒体项是否可以播放，播放列表可能包含不可播放的媒体项目，以显示禁用的媒体项目
> - **Duration**：持续时间是内容的长度，**MediaController 只能通过元数据获取时长**，这告诉播放何时结束，也告诉 MediaController.seekTo(long) 的允许范围，如果开发人员未设置，MediaSession 将使用 SessionPlayer.getDuration() 更新元数据中的持续时间
> - **User rating**：用户评级

- MediaMetadata.Builder
	>	构建MediaMetadata对象

- BROWSABLE_TYPE_ALBUMS
	> 包含按专辑分类的可浏览项目的可浏览类型

- BROWSABLE_TYPE_ARTISTS
	> 包含按艺术家分类的可浏览项目的可浏览类型

- BROWSABLE_TYPE_GENRES

- BROWSABLE_TYPE_MIXED

- BROWSABLE_TYPE_NONE
- BROWSABLE_TYPE_PLAYLISTS
- BROWSABLE_TYPE_TITLES
- BROWSABLE_TYPE_YEARS

- STATUS_DOWNLOADED
- STATUS_DOWNLOADING
- STATUS_NOT_DOWNLOADED



- METADATA_KEY_ADVERTISEMENT
	> **Long**，检索媒体是否是广告的信息，0：不是广告，1或非0：是广告

- METADATA_KEY_ALBUM
	> **String**，专辑标题信息

- METADATA_KEY_ALBUM_ART
	> **Bitmap**，检索有关媒体原始来源专辑的艺术作品的信息，艺术品应该相对较小，如果太大，可以按比例缩小，对于更高分辨率的艺术品，应该使用 METADATA_KEY_ALBUM_ART_URI。

- METADATA_KEY_ALBUM_ART_URI
	> **Bitmap**，检索有关媒体原始来源专辑的艺术作品的信息

- METADATA_KEY_ART
  > **Bitmap**：检索有关媒体作品的信息，艺术品应该相对较小，如果太大，可以按比例缩小，对于更高分辨率的艺术品，应该使用 METADATA_KEY_ART_URI

- METADATA_KEY_ART_URI
	> **Bitmap**：检索有关媒体作品的信息

- METADATA_KEY_ALBUM_ARTIST
  > **String**：检索有关媒体原始来源专辑的艺术家的信息（专辑艺术家）

- METADATA_KEY_ARTIST
  > **String**：检索有关媒体艺术家的信息（歌手）

- METADATA_KEY_AUTHOR
  > **String**：检索有关媒体作者的信息（创作者）

- METADATA_KEY_BROWSABLE
  > **Long**，检索关于可浏览类型的信息，值 **BROWSABLE_TYPE_***

- METADATA_KEY_COMPILATION
	> **String**，检索有关媒体编译状态的信息

- METADATA_KEY_COMPOSER
  > **String**，检索关于媒体的编写器的信息

- METADATA_KEY_DATE
  > **String**，检索有关媒体创建或发布日期的信息，格式不指定，推荐使用RFC3339

- METADATA_KEY_DISC_NUMBER
  > **Long**，检索关于介质原始来源的磁盘号的信息

- METADATA_KEY_DISPLAY_DESCRIPTION
  > **String**，检索适合显示给用户的描述信息，当显示此元数据所描述的媒体的更多信息时，如果有其他字段，则应优先选择此字段

- METADATA_KEY_DISPLAY_ICON
	> **Bitmap**：检索适合显示给用户的图标或缩略图的信息，当显示由该元数据描述的媒体图标时，如果有其他字段，应优先选择该字段，对于更高分辨率的艺术品，METADATA_KEY_DISPLAY_ICON_URI应该被使用

- METADATA_KEY_DISPLAY_ICON_URI
  > **Bitmap**：检索适合显示给用户的图标或缩略图的信息

- METADATA_KEY_DISPLAY_SUBTITLE
  > **String**，检索适合显示给用户的副标题信息，当显示由该元数据描述的媒体的第二行时，如果有其他字段，应该优先使用该字段。

- METADATA_KEY_DISPLAY_TITLE
  > **String**，检索适合显示给用户的标题信息，它通常与METADATA_KEY_TITLE相同，但在某些格式下可能有所不同，在显示由该元数据描述的媒体时，如果存在该元数据，则应首选该元数据

- METADATA_KEY_DOWNLOAD_STATUS
  > **Long**，检索有关媒体下载状态的信息，这些信息将用于以后的脱机回放，值 **STATUS_*** 

- METADATA_KEY_DURATION
	> **Long**，检索有关ms.介质持续时间的信息，负值持续时间表示持续时间未知(或无限)

- METADATA_KEY_EXTRAS
  > A Bundle extra. **metadata.getExtras()**

- METADATA_KEY_GENRE
	> **String**，检索有关媒体类型的信息

- METADATA_KEY_MEDIA_ID
  > **String**，检索有关内容的媒体ID的信息

- METADATA_KEY_MEDIA_URI
  > **String**，检索内容的Uri信息

- METADATA_KEY_NUM_TRACKS
  > **Long**，检索有关媒体原始来源中音轨数量的信息

- METADATA_KEY_PLAYABLE
  > **Long**，检索有关媒体是否可播放的信息，值为0表示它不是可播放项目，值为1或非零表示它可播放的

- METADATA_KEY_RATING
  > 检索关于媒体的总体评级的信息 **getRating()**

- METADATA_KEY_TITLE
  > **String**，检索有关媒体标题的信息

- METADATA_KEY_TRACK_NUMBER
  > **Long**，检索有关媒体的曲目号的信息
  
- METADATA_KEY_USER_RATING
  > 检索有关用户对媒体评级的信息 **getRating()**

- METADATA_KEY_WRITER
  > **String**，检索有关媒体写入者的信息

- METADATA_KEY_YEAR
  > **Long**，检索关于创建或发布媒体的年份的信息



## (abstract) MediaController.ControllerCallback

- 

## (class) MediaBrowser.BrowserCallback

**extends** [MediaController.ControllerCallback](#MediaController.ControllerCallback)

> 对MediaLibraryService

- **onChildrenChanged**(MediaBrowser browser, String parentId, int itemCount, MediaLibraryService.LibraryParams params) 
- **onSearchResultChanged**(MediaBrowser browser, String query, int itemCount, MediaLibraryService.LibraryParams params) 


## MediaController



## MediaBrowser

**extends** [MediaController](#MediaController)

> 用于浏览由 *MediaLibraryService* 提供的媒体内容的





    <service
            android:process="@string/process"
            android:name=".avrcpcontroller.BluetoothMediaBrowserService"
            android:exported="true"
            android:enabled="@bool/profile_supported_a2dp_sink"
            android:label="@string/a2dp_sink_mbs_label">
            <intent-filter>
                <action android:name="android.media.browse.MediaBrowserService" />
            </intent-filter>
        </service>