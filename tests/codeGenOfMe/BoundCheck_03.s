.file "tests/codeGenOfMe/BoundCheck_03.dcf"
.text
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $96, %rsp
movq $9,-80(%rbp)
movq $0,-88(%rbp)
.L1:
movq -88(%rbp),%rax
movq -80(%rbp),%r10
cmpq %r10,%rax
jge .L2
movq -88(%rbp),%rax
movq -88(%rbp),%r10
movq %r10,-72(%rbp,%rax,8)
movq -88(%rbp),%rax
movq $1,%r10
addq %r10,%rax
movq %rax,-88(%rbp)
jmp .L1
.L2:
leave
ret

