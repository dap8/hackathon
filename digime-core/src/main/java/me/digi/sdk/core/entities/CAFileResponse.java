/*
 * Copyright © 2017 digi.me. All rights reserved.
 */

package me.digi.sdk.core.entities;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CAFileResponse {

    @SerializedName("fileContent")
    public List<CAContent> fileContent;

    @SerializedName("fileList")
    public List<String> fileIds;
}
