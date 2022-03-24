package io.github.cloudate9.endermaning.config;

import com.github.secretx33.sccfg.api.annotation.Comment;
import com.github.secretx33.sccfg.api.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration("hybridConfig.yml")
public class HybridConfig {

    @Comment("This contains the list of all hybrids.")
    public List<String> hybridList = new ArrayList<>();

    @Comment({
            "This contains all hybrids declared using a previous major version of Endermaning",
            "This allows for the removal of items that was used previously, but no longer necessary"
    })
    public List<String> legacyHybridList = new ArrayList<>();

    @Comment({
            "This contains all hybrids that were made hybrids when they were offline.",
            "All values in this list MUST be in hybridList as well!"
    })
    public List<String> uninformedHybridList = new ArrayList<>();

    @Comment({
            "This contains all hybrids that were reverted to human status when they were offline.",
            "All values in this list should not be in hybridList."
    })
    public List<String> uninformedRevertedList = new ArrayList<>();
}

