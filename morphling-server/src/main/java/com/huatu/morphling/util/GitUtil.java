package com.huatu.morphling.util;

import com.huatu.morphling.common.bean.GitTag;
import com.huatu.morphling.utils.date.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.TagOpt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/12/15 13:12
 */
@Slf4j
public class GitUtil {
    public static List<GitTag> listGitTags(String gitDir,String gitUrl) throws IOException, GitAPIException {
        File file = new File(gitDir);
        Git git = null;
        if(!file.exists()){
            file.mkdirs();
            InitCommand init = Git.init();
            init.setDirectory(file);
            git = init.call();
            StoredConfig config = git.getRepository().getConfig();
            config.setString("remote","origin","url",gitUrl);
            config.setString("remote","origin","fetch","+refs/heads/*:refs/remotes/origin/*");
            config.save();
            FetchCommand fetch = git.fetch();
            fetch.setRefSpecs(new RefSpec("+refs/heads/*:refs/remotes/origin/*"));
            fetch.setRemote(gitUrl);
            fetch.setTagOpt(TagOpt.FETCH_TAGS);
            fetch.call();
        }else{
            git = Git.open(file);
        }
        FetchCommand fetch = git.fetch();
        fetch.setRefSpecs(new RefSpec("+refs/heads/*:refs/remotes/origin/*"));
        fetch.setRemote(gitUrl);
        fetch.setTagOpt(TagOpt.FETCH_TAGS);
        fetch.call();
        List<Ref> list = git.tagList().call();
        RevWalk walk = new RevWalk(git.getRepository());
        if (list == null) {
            return new ArrayList<GitTag>();
        }
        List<GitTag> tags = new ArrayList<GitTag>(list.size());
        for (Ref ref : list) {
            RevTag tag = walk.parseTag(ref.getObjectId());
            GitTag tagInfo = parseTag(tag);
            tags.add(tagInfo);
        }
        List<GitTag> collect = tags.stream().sorted((o1, o2) -> {
            return o2.getCreateTime().compareTo(o1.getCreateTime());
        }).limit(20).collect(Collectors.toList());
        return collect;
    }

    private static GitTag parseTag(RevTag tag){
        GitTag tagInfo = new GitTag();
        Date when = tag.getTaggerIdent().getWhen();
        tagInfo.setCreateTime(DateFormatUtil.DEFAULT_FORMAT.format(when));
        tagInfo.setRemark(tag.getShortMessage());

        String fullMsg = tag.getFullMessage();
        String[] arr = fullMsg.split("##", 4);
        tagInfo.setTagName(tag.getTagName());
        if (arr.length == 4) {
            tagInfo.setModule(arr[0]);
            tagInfo.setCreateBy(arr[1]);
            tagInfo.setBranch(arr[3]);
        } else if(arr.length==3) {
            tagInfo.setModule(arr[0]);
            tagInfo.setCreateBy(arr[1]);
        }
        return tagInfo;
    }

}
