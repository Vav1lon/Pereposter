package com.pereposter.web.service

import com.pereposter.web.entity.SocialNetwork
import com.pereposter.web.entity.SocialNetworkEnum
import com.pereposter.web.entity.User
import com.pereposter.web.entity.UserSocialAccount

class SocialBoardService {

    def springSecurityService

    List<SocialNetwork> list() {
        currentUser.accounts.collect {
            new SocialNetwork(socialNetworkEnum: it.socialNetwork
                    , id: it.id
                    , username: it.username
                    , userId: getCurrentUser().id
                    , enabled: it.enabled)
        }
    }

    long addSocialNetwork(Integer socialNetworkId, String username, String password) {

        UserSocialAccount account = new UserSocialAccount(
                username: username
                , enabled: false
                , password: password
                , socialNetwork: SocialNetworkEnum.fromInt(socialNetworkId)
        )

        getCurrentUser().addToAccounts(account).save()
        account.save()
        account.id
    }

    long updateSocialNetwork(long socialNetworkId, SocialNetworkEnum socialNetwork, String username, String password) {

        UserSocialAccount socialNetworkInternal = getCurrentUser().accounts.find { it.id = socialNetworkId }

        if (!socialNetworkInternal) {
            throw new NullPointerException("Not found UserSocialAccount by id: " + socialNetworkId + " in User: " + getCurrentUser().id);
        }

        removedSocialNetwork(socialNetworkId)
        addSocialNetwork(socialNetwork, username, password)
    }

    void enabledAndDisabledSocialNetwork(long socialNetworkId) {

        UserSocialAccount socialNetwork = getCurrentUser().accounts.find { it.id = socialNetworkId }

        if (!socialNetwork) {
            throw new NullPointerException("Not found UserSocialAccount by id: " + socialNetworkId + " in User: " + getCurrentUser().id);
        }

        if (socialNetwork.enabled) {
            socialNetwork.enabled = false
        } else {
            socialNetwork.enabled = true
        }

        socialNetwork.save()
    }

    void removedSocialNetwork(long socialNetworkId) {
        UserSocialAccount socialNetwork = getCurrentUser().accounts.find { it.id = socialNetworkId }

        if (!socialNetwork) {
            throw new NullPointerException("Not found UserSocialAccount by id: " + socialNetworkId + " in User: " + getCurrentUser().id);
        }
        getCurrentUser().removeFromAccounts(socialNetwork)
        socialNetwork.delete()
    }

    private User getCurrentUser() {
        User.findById(springSecurityService.currentUser?.id)
    }

}
