package com.xebia.devradar;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GithubFetcherIntegrationTest {

    @Test
    public void fetch() {

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);

        GithubCommitsDTO commits =
                Client.create(clientConfig)
                        .resource("http://github.com/api/v2/json/commits/list/xebia-france/dev-radar/master")
                        .get(GithubCommitsDTO.class);

        for (GithubCommitDto dto : commits.commits) {
            System.out.println(dto);
        }
    }

    public static class GithubCommitsDTO {
        public List<GithubCommitDto> commits;
    }

    public static class GithubCommitDto {
        public final String[] parents;
        public final GithubPerson author;
        public final String url;
        public final String id;
        public final String committed_date;
        public final String authored_date;
        public final String message;
        public final String tree;
        public final GithubPerson committer;

        public GithubCommitDto(String[] parents, GithubPerson author, String url, String id, String committed_date, String authored_date, String message, String tree, GithubPerson committer) {
            this.parents = parents;
            this.author = author;
            this.url = url;
            this.id = id;
            this.committed_date = committed_date;
            this.authored_date = authored_date;
            this.message = message;
            this.tree = tree;
            this.committer = committer;
        }

        @Override
        public String toString() {
            return "GithubCommitDto{" +
                    "parents=" + (parents == null ? null : Arrays.asList(parents)) +
                    ", author=" + author +
                    ", url='" + url + '\'' +
                    ", id='" + id + '\'' +
                    ", committed_date='" + committed_date + '\'' +
                    ", authored_date='" + authored_date + '\'' +
                    ", message='" + message + '\'' +
                    ", tree='" + tree + '\'' +
                    ", committer=" + committer +
                    '}';
        }
    }

    public static class GithubPerson {
        public final String name;
        public final String login;
        public final String email;

        public GithubPerson(String name, String login, String email) {
            this.name = name;
            this.login = login;
            this.email = email;
        }

        @Override
        public String toString() {
            return "GithubPerson{" +
                    "name='" + name + '\'' +
                    ", login='" + login + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}
