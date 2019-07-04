.file "tests/codeGenOfMe/NotOutOfCtrl.dcf"
.text
.text
.globl func
.type func, @function
func:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
movq %rdi,-48(%rbp)
movq %rsi,-40(%rbp)
movq -48(%rbp),%rax
movq -40(%rbp),%r10
cmpq %r10,%rax
jle .L5
movq -32(%rbp),%rax
movq -24(%rbp),%r10
cmpq %r10,%rax
jle .L6
movq -32(%rbp),%r10
movq %r10,%rax
jmp .L3
jmp .L3
.L6:
movq $5,%rax
jmp .L3
jmp .L3
.L5:
movq -32(%rbp),%rax
movq -24(%rbp),%r10
cmpq %r10,%rax
jle .L7
movq -48(%rbp),%rax
movq -40(%rbp),%r10
cmpq %r10,%rax
jne .L8
movq $5,%rax
jmp .L3
jmp .L3
.L8:
movq $0,%rax
jmp .L3
jmp .L3
.L7:
movq -16(%rbp),%rax
movq -8(%rbp),%r10
cmpq %r10,%rax
jle .L9
movq $2,%rax
jmp .L3
jmp .L3
.L9:
movq $3,%rax
movq $8,%r10
cmpq %r10,%rax
jle .L10
movq $6,%rax
jmp .L3
jmp .L3
.L10:
movq $8,%rax
jmp .L3
.L3:
leave
ret
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
.L4:
leave
ret

