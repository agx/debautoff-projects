def gitbranch = 'debian/sid'
def recipient = "agx@sigxcpu.org"
def pkgs = [
        'cups-pk-helper'      : 'https://anonscm.debian.org/git/users/agx/cups-pk-helper.git',
	'gtk-vnc'             : 'https://anonscm.debian.org/git/pkg-libvirt/gtk-vnc.git',
        'krb5-auth-dialog'    : 'https://anonscm.debian.org/git/users/agx/krb5-auth-dialog.git',
        'libosinfo'           : 'https://anonscm.debian.org/git/pkg-libvirt/libosinfo.git',
        'libvirt-glib'        : 'https://anonscm.debian.org/git/git/pkg-libvirt/libvirt-glib.git',
        'libvirt-python'      : 'https://anonscm.debian.org/git/pkg-libvirt/libvirt-python.git',
	'nosexcover'          : 'https://anonscm.debian.org/git/users/agx/nosexcover.git',
        'osinfo-db-tools'     : 'https://anonscm.debian.org/git/pkg-libvirt/osinfo-db-tools.git',
        'pykerberos'          : 'https://anonscm.debian.org/git/calendarserver/pykerberos.git',
	'python-dateutil'     : 'https://anonscm.debian.org/git/calendarserver/python-dateutil.git',
	'python-vobject'      : 'https://anonscm.debian.org/git/calendarserver/python-vobject.git',
        'ruby-libvirt'        : 'https://anonscm.debian.org/git/pkg-libvirt/ruby-libvirt.git',
        'virt-manager'        : 'https://anonscm.debian.org/git/pkg-libvirt/virt-manager.git',
        'virt-top'            : 'https://anonscm.debian.org/git/pkg-ocaml-maint/packages/virt-top.git',
        'virt-viewer'         : 'https://anonscm.debian.org/git/pkg-libvirt/virt-viewer.git',
        'virt-what'           : 'https://anonscm.debian.org/git/pkg-libvirt/virt-what.git',
       ]

pkgs.each { pkg ->
  freeStyleJob(pkg.key) {
    triggers {
      cron('H H * * *')
    }
    steps {
       shell('WORKDIR=tryff\n' +
         'if [ ! -d "${WORKDIR}" ]; then\n' +
         '  gbp clone ' + pkg.value  + ' "${WORKDIR}"\n' +
         '  cd "${WORKDIR}"\n' +
         'else\n' +
         '  cd "${WORKDIR}"\n' +
         '  git checkout -f\n' +
         '  gbp pull\n' +
         'fi\n' +
         '/usr/share/doc/git-buildpackage/examples/gbp-try-ff\n')
    }
    publishers {
        mailer(recipient, true, false)
        // We're creating commits during each build.
        wsCleanup()
    }
  }
}

