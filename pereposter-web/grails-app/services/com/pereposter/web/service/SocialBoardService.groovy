package com.pereposter.web.service

import com.pereposter.web.entity.SocialNetwork
import com.pereposter.web.entity.SocialNetworkEnum
import com.pereposter.web.entity.User
import com.pereposter.web.entity.SocialAccount

class SocialBoardService {

    def springSecurityService

    List<SocialNetwork> list() {
        currentUser.accounts.collect {
            new SocialNetwork(socialNetworkEnum: it.socialNetwork
                    , id: it.id
                    , name: it.name
                    , username: it.username
                    , userId: getCurrentUser().id
                    , enabled: it.enabled
                    , socialUserId: it.socialUserId
                    , lastPostId: it.lastPostId)
        }
    }

    long addSocialNetwork(Integer socialNetworkId, String username, String password, String name) {

        SocialAccount account = new SocialAccount(
                username: username
                , enabled: false
                , password: password
                , socialNetwork: SocialNetworkEnum.fromInt(socialNetworkId)
                , name: name
        )

        getCurrentUser().addToAccounts(account).save()
        account.save()
        account.id
    }

    long updateSocialNetwork(long socialNetworkId, SocialNetworkEnum socialNetwork, String username, String password) {

        SocialAccount socialNetworkInternal = SocialAccount.findByIdAndUser(socialNetworkId, getCurrentUser())

        if (!socialNetworkInternal) {
            throw new NullPointerException("Not found SocialAccount by id: " + socialNetworkId + " in User: " + getCurrentUser().id);
        }

        removedSocialNetwork(socialNetworkId)
        addSocialNetwork(socialNetwork, username, password)
    }

    void enabledAndDisabledSocialNetwork(long socialNetworkId) {

        SocialAccount socialNetwork = SocialAccount.findByIdAndUser(socialNetworkId, getCurrentUser())

        if (!socialNetwork) {
            throw new NullPointerException("Not found SocialAccount by id: " + socialNetworkId + " in User: " + getCurrentUser().id);
        }

        if (socialNetwork.enabled) {
            socialNetwork.enabled = false
        } else {
            socialNetwork.enabled = true
        }

        socialNetwork.save()
    }

    void removedSocialNetwork(long socialNetworkId) {
        SocialAccount socialNetwork = getCurrentUser().accounts.find { it.id = socialNetworkId }

        if (!socialNetwork) {
            throw new NullPointerException("Not found SocialAccount by id: " + socialNetworkId + " in User: " + getCurrentUser().id);
        }

        User currentUser = socialNetwork.user;

        currentUser.removeFromAccounts(socialNetwork)
        currentUser.save()
    }

    private User getCurrentUser() {
        User.findById(springSecurityService.currentUser?.id)
    }

}
