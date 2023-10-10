#!/bin/sh

cd ..

function setup_hook()
{
    filename=$1
    [[ -f .git/hooks/$filename ]]; rm -f .git/hooks/$filename
    ln -s ../../githooks/hooks/$filename       .git/hooks/$filename
    chmod +x githooks/hooks/$filename

    return 0;
}

git config --global core.filemode false

# The unsupported hooks!
# setup_hook post-commit
# setup_hook update

# The Ok hooks!
# setup_hook applypatch-msg      # APPLYPATCH_MSG(GIT, commit-msg-file)
setup_hook commit-msg          # COMMIT_MSG(GIT, commit-msg-file)
# setup_hook post_applypatch     # POST_APPLYPATCH(GIT)
# setup_hook post-checkout       # POST_CHECKOUT(GIT, prev-head-ref, new-head-ref, is-branch-checkout)
# setup_hook post-merge          # POST_MERGE(GIT, is-squash-merge)
# setup_hook post-update         # POST_COMMIT(GIT)
# setup_hook pre-applypatch      # PRE_APPLYPATCH(GIT)
# setup_hook pre-commit          # PRE_COMMIT(GIT)
setup_hook prepare-commit-msg  # PREPARE_COMMIT_MSG(GIT, commit-msg-file [, msg-src [, SHA1]])
# setup_hook pre-push            # PRE_PUSH(GIT, remote-name, remote-url)
# setup_hook pre-rebase          # PRE_REBASE(GIT, upstream [, branch])

exit 0
