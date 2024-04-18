package org.projectcheckins.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailInvitationConfigPropertiesTest {

    @Test
    void settersAndGetters() {
        var conf = new EmailInvitationConfigProperties();
        assertThat(conf.getUrl()).isEmpty();
        conf.setUrl("http://example.com/signUp");
        assertThat(conf.getUrl()).isEqualTo("http://example.com/signUp");
    }
}
