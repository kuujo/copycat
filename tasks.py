import os

from invoke import task

# SERVICE SPECIFIC VALUES
# These values can be set to facilitate local builds
# CirceCI config must specify all the values when calling invoke tasks
SERVICE = "o1t-netconf"
CI_GLUE_SERVICE_NAME = "o1t-netconf-container"
SERVICE_HYPHENATED = SERVICE.replace("_","-")
#TEST_IMAGE_NAME = SERVICE+"_test"
#DOCKER_FILE_TEST_PATH = "Dockerfile.test"
DOCKER_BUILD_SUBDIRECTORY = "./"
ECR_REPO = "728074501999.dkr.ecr.eu-west-1.amazonaws.com/"
DOCKER_BUILD_ARGS = {}
ECR_SERVICE_NAME = 'inmarsat/5g-sdn/' + SERVICE_HYPHENATED
VERSION = os.popen('cicd version current-version').read().strip()
MANIFEST_FILE = "manifest.txt"


# --------- IF POSSIBLE DON'T MAKE CHANGES BELOW ---------

@task
def setup(c):
    c.run(f'echo "export GOPATH=~/go" >> "$BASH_ENV"')
    c.run(f'ssh-keyscan github.com >> ~/.ssh/known_hosts')
    c.run(f'git config --global url.ssh://git@github.com/Inmarsat.insteadOf https://github.com/Inmarsat')

@task
def gosec(c, path=None):
    command = f'/go/bin/gosec ./...'
    c.run(command)

@task
def test(c):
    command = f'go test ./...'
    c.run(command)

@task
def docker_build(c, ssh_key_path=None):
    if ssh_key_path is None:
        ssh_key_path = '~/.ssh/id_rsa'
    c.run('echo RUNNING TASK: docker_build')
    c.run(f'ssh-add -D')
    c.run(f'ssh-add {ssh_key_path}')
    command = f'docker build --ssh default . -t {ECR_SERVICE_NAME}:{VERSION} -f ./cmd/o1t_netconf/Dockerfile'
    c.run(command)

@task
def get_change_id(c):
    print('RUNNING TASK: get-change-id')
    CHANGE_ID_FILE = "change_id"
    if not os.path.isfile(CHANGE_ID_FILE):
        c.run(f'cicd glue create-change | jq -r .id | tee {CHANGE_ID_FILE}')
    change_id = open(CHANGE_ID_FILE, "r").read().strip()
    return change_id



### Replace {SERVICE} from publish_to_ci_glue with the call of this function in case you need to publish a hyphenated service ###
@task
def get_hyphenated_component_name(c):
    return SERVICE_HYPHENATED


@task
def publish_to_ci_glue(c):
    print('RUNNING TASK: publish-to-ci-glue')
    command = f'cicd glue create-version --component "{CI_GLUE_SERVICE_NAME}" --version-string "{VERSION}" ' \
              f'--change-id "{get_change_id(c)}" --manifest-file "{MANIFEST_FILE}"'
    c.run(command)



@task
def publish_to_jira(c):
    print('RUNNING TASK: publish-to-jira')
    command = f'cicd -vv jira record-version --component {SERVICE} ' \
              f'--version-string {VERSION} --field component'
    c.run(command)



@task
def publish_to_repo(c):
    print('RUNNING TASK: publish-to-repo')
    command = f'cicd deploy publish --definition version_string="{VERSION}" --definition change_id="{get_change_id(c)}"'
    c.run(command)



@task
def get_ecr_name(c):
    print(ECR_SERVICE_NAME)



@task
def get_version(c):
    print(VERSION)



@task
def create_tag(c):
    c.run('echo RUNNING TASK: create-tag')
    c.run('cicd version create-tag --only-if-necessary')
    c.run('git push --tags')
